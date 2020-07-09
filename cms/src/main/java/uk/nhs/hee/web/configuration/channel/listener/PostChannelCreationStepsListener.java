package uk.nhs.hee.web.configuration.channel.listener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.value.ValueFactoryImpl;
import org.hippoecm.hst.configuration.channel.ChannelManagerEvent;
import org.hippoecm.hst.configuration.channel.ChannelManagerEventListener;
import org.hippoecm.hst.configuration.channel.ChannelManagerEventListenerException;
import org.hippoecm.hst.container.RequestContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.nhs.hee.web.yaml.service.ImportService;

public class PostChannelCreationStepsListener implements ChannelManagerEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostChannelCreationStepsListener.class);

    private String blueprintId;
    private String regionId;
    private String regionName;
    private Session jcrSession;
    private Map<String, String> placeholderValues;

    @Override
    public void channelCreated(ChannelManagerEvent event)
            throws ChannelManagerEventListenerException {
        importBlueprintYamlFiles(event);
    }

    @Override
    public void channelUpdated(ChannelManagerEvent event) {
    }

    private void importBlueprintYamlFiles(ChannelManagerEvent event) {
        blueprintId = event.getBlueprint().getId();
        regionId = event.getChannel().getId();
        regionName = event.getChannel().getName();

        try {
            jcrSession = RequestContextProvider.get().getSession();
        } catch (RepositoryException e) {
            LOGGER.error("Caught error '{}' while getting Jcr Session", e.getMessage(), e);
            return;
        }

        // Initialises regional placeholder values (regionId & regionName)
        initPlaceholderValues();

        // Import the yaml files under 'regional-blueprint/' classpath resource
        importBlueprintYamlFiles();

        // Adding regional-users onto 'global-viewer' group
        addRegionalAuthorAndEditorToGlobalViewerGroup();

        // Updating placeholders (e.g. <regionName>, etc) on the node properties
        // provided in 'regional-blueprint/node-properties-with-regional-placeholders.txt'
        updateRegionalPlaceholdersOnNodeProperties();

        // Updating placeholders (e.g. <regionName>, etc) on the node properties
        // provided in 'regional-blueprint/content-node-properties-with-regional-placeholders.txt'
        updateRegionalPlaceholdersOnContentNodeProperties();

        // Finally saving the session to update repo.
        try {
            jcrSession.save();
        } catch(RepositoryException e) {
            LOGGER.error("Caught error '{}' while saving the Jcr Session", e.getMessage(), e);
        }
    }

    private void importBlueprintYamlFiles() {
        List<String> bluePrintYamlFilePaths = getBluePrintYamlFilePaths();

        if (bluePrintYamlFilePaths == null || bluePrintYamlFilePaths.isEmpty()) {
            LOGGER.debug("No Yaml files to import under '{}/' Blueprint resource path", blueprintId);
            return;
        }

        for (String bluePrintYamlFilePath : bluePrintYamlFilePaths) {
            try {
                String plainYaml = getRegionalPlaceholderUpdatedYamlString(bluePrintYamlFilePath, regionId);

                if (StringUtils.isEmpty(plainYaml)) {
                    return;
                }

                String nodePath = plainYaml.split(System.getProperty("line.separator"))[0];
                String parentNodePath = nodePath.substring(0, nodePath.lastIndexOf('/'));

                ImportService.importPlainYaml(
                        new ByteArrayInputStream(plainYaml.getBytes(StandardCharsets.UTF_8)),
                        parentNodePath);
            } catch (RepositoryException | IOException e) {
                LOGGER.error(
                        "Caught error '{}' while importing the Yaml file '{}'",
                        e.getMessage(),
                        bluePrintYamlFilePath,
                        e);
            }
        }
    }

    private String getRegionalPlaceholderUpdatedYamlString(String bluePrintYamlFilePath, String regionId)
            throws IOException {
        String plainYaml = IOUtils.toString(
                this.getClass().getClassLoader()
                .getResourceAsStream(bluePrintYamlFilePath), StandardCharsets.UTF_8);

        LOGGER.debug("'{}' file content => {}", bluePrintYamlFilePath, plainYaml);

        if (StringUtils.isEmpty(plainYaml)) {
            LOGGER.debug("'{}' file is empty", bluePrintYamlFilePath);
            return StringUtils.EMPTY;
        }

        String placeholderUpdatedPlainYaml = plainYaml.replace("{{ regionId }}", regionId);

        LOGGER.debug(
                "'{}' file content after 'regionId={}' placeholder update => {}",
                bluePrintYamlFilePath,
                regionId,
                placeholderUpdatedPlainYaml);

        return placeholderUpdatedPlainYaml;
    }

    private List<String> getBluePrintYamlFilePaths() {
        CodeSource codeSrc = PostChannelCreationStepsListener.class.getProtectionDomain().getCodeSource();
        List<String> yamlFilePaths = new ArrayList<String>();

        if (codeSrc != null) {
            try {
                URL jar = codeSrc.getLocation();
                ZipInputStream zipInputStream = new ZipInputStream(jar.openStream());
                ZipEntry zipEntry = null;

                while((zipEntry = zipInputStream.getNextEntry() ) != null) {
                    String entryName = zipEntry.getName();

                    if (entryName.startsWith(blueprintId) &&
                            (entryName.endsWith(".yaml") || entryName.endsWith(".yml"))) {
                        yamlFilePaths.add(entryName);
                    }
                }
            } catch (IOException e) {
                LOGGER.error(
                        "Caught error '{}' while reading Yaml file under the Blueprint resource path '{}/'",
                        e.getMessage(),
                        blueprintId,
                        e);

                return null;
            }
         }

        LOGGER.debug("Found {} Yaml files under the Blueprint resource path '{}/'", yamlFilePaths, blueprintId);

        return yamlFilePaths;
    }

    private void addRegionalAuthorAndEditorToGlobalViewerGroup() {
        String hippoSysMembersProperty = "hipposys:members";
        String globalViewerGroupNode = "/hippo:configuration/hippo:groups/global-viewer";

        try {
            Node globalViewer = jcrSession.getNode(globalViewerGroupNode);
            Value[] existingMembers = globalViewer.getProperty(hippoSysMembersProperty).getValues();
            Value[] updatedMembers = ArrayUtils.addAll(
                    existingMembers,
                    ValueFactoryImpl.getInstance().createValue(regionId + "-author"),
                    ValueFactoryImpl.getInstance().createValue(regionId + "-editor"));

            globalViewer.setProperty(hippoSysMembersProperty, updatedMembers, 1);
        } catch (RepositoryException e) {
            LOGGER.error(
                    "Caught error '{}' while adding [{}-author, {}-editor] members to Group '{}'",
                    e.getMessage(),
                    regionId,
                    regionId,
                    globalViewerGroupNode,
                    e);
        }
    }

    private void updateRegionalPlaceholdersOnNodeProperties() {
        String nodePropertiesWithPlaceholdersFilePath =
                blueprintId + "/node-properties-with-regional-placeholders.txt";
        String nodePropertiesWithPlaceholdersUpdated =
                readResourceContentWithPlaceholdersUpdated(nodePropertiesWithPlaceholdersFilePath);

        for (String nodePropertyQN :
            nodePropertiesWithPlaceholdersUpdated.split(System.getProperty("line.separator"))) {
            String nodePath = nodePropertyQN.substring(0, nodePropertyQN.lastIndexOf('/'));
            String propertyName = nodePropertyQN.substring(nodePropertyQN.lastIndexOf("/@") + 2);

            try {
                if (!jcrSession.nodeExists(nodePath)) {
                    LOGGER.warn("Node path '{}' doesn't exists. Please verify.", nodePath);
                    continue;
                }

                Node node = jcrSession.getNode(nodePath);
                setPlaceholderUpdatedProperty(jcrSession.getNode(nodePath), node.getProperty(propertyName));
            } catch (RepositoryException e) {
                LOGGER.error(
                        "Caught error '{}' while updating <regionName> placeholder on the node property '{}'",
                        e.getMessage(),
                        nodePropertyQN,
                        e);
            }
        }
    }

    private void updateRegionalPlaceholdersOnContentNodeProperties() {
        String contentNodePropertiesWithPlaceholdersFilePath =
                blueprintId + "/content-node-properties-with-regional-placeholders.txt";
        String nodePropertiesWithPlaceholdersUpdated =
                readResourceContentWithPlaceholdersUpdated(contentNodePropertiesWithPlaceholdersFilePath);

        for (String nodePropertyQN :
            nodePropertiesWithPlaceholdersUpdated.split(System.getProperty("line.separator"))) {
            String nodePath = nodePropertyQN.substring(0, nodePropertyQN.lastIndexOf('/'));
            String nodeParentPath = nodePath.substring(0, nodePath.lastIndexOf('/'));
            String nodeName = nodePath.substring(nodePath.lastIndexOf("/") + 1);
            String propertyName = nodePropertyQN.substring(nodePropertyQN.lastIndexOf("/@") + 2);

            try {
                if (!jcrSession.nodeExists(nodeParentPath)) {
                    LOGGER.warn("Node path '{}' doesn't exists. Please verify.", nodePath);
                    continue;
                }

                Node parentNode = jcrSession.getNode(nodeParentPath);
                NodeIterator parentNodeIterator = parentNode.getNodes();

                // Updates placeholders on all 3 versions of the content (draft, unpublished & published states)
                while (parentNodeIterator.hasNext()) {
                  Node contentNode = parentNodeIterator.nextNode();

                  if (nodeName.equals(contentNode.getName())) {
                      setPlaceholderUpdatedProperty(contentNode, contentNode.getProperty(propertyName));
                  }
                }
            } catch (RepositoryException e) {
                LOGGER.error(
                        "Caught error '{}' while updating <regionName> placeholder on the node property '{}'",
                        e.getMessage(),
                        nodePropertyQN,
                        e);
            }
        }
    }

    private void setPlaceholderUpdatedProperty(Node node, Property property) {
        try {
            if (property.isMultiple()) {
                List<String> updatedPropertyValues = new ArrayList<String>();
                Value[] existingValues = property.getValues();
                Value[] updatedValues = new Value[existingValues.length];

                for (Value existingValue : existingValues) {
                    String updatedPropertyValue =
                            substituteRegionalPlaceholders(existingValue.getString(), placeholderValues);
                    updatedValues = (Value[]) ArrayUtils.add(
                            updatedValues,
                            ValueFactoryImpl.getInstance().createValue(updatedPropertyValue));
                    updatedPropertyValues.add(updatedPropertyValue);
                }

                node.setProperty(property.getName(), updatedValues, 1);

                LOGGER.debug(
                        "'{}/@{}' node property has been updated to '{}'",
                        node.getPath(),
                        property.getName(),
                        updatedPropertyValues);
            } else {
                String propertyValue = node.getProperty(property.getName()).getString();
                String updatedPropertyValue =
                        substituteRegionalPlaceholders(propertyValue, placeholderValues);

                node.setProperty(property.getName(), updatedPropertyValue, 1);

                LOGGER.debug(
                        "'{}/@{}' node property has been updated to '{}'",
                        node.getPath(),
                        property.getName(),
                        updatedPropertyValue);
            }
        } catch (RepositoryException e) {
            try {
                LOGGER.error(
                        "Caught error '{}' while updating regional placeholders property '{}' on the node '{}'",
                        e.getMessage(),
                        property.getName(),
                        node.getPath(),
                        e);
            } catch(RepositoryException re) {
                // Ignore silently
            }
        }
    }

    private String readResourceContentWithPlaceholdersUpdated(String resourcePath) {
        InputStream resourceStream =
                this.getClass().getClassLoader()
                .getResourceAsStream(resourcePath);

        if (resourceStream == null) {
            LOGGER.debug("'{}' isn't available in the classpath", resourcePath);
            return StringUtils.EMPTY;
        }

        String resourceContentWithPlaceholders = StringUtils.EMPTY;
        try {
            resourceContentWithPlaceholders = IOUtils.toString(resourceStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error(
                    "Caught error '{}' while reading the content of the file '{}'",
                    e.getMessage(),
                    resourcePath,
                    e);
            return StringUtils.EMPTY;
        }

        if (StringUtils.isEmpty(resourceContentWithPlaceholders)) {
            LOGGER.debug("'{}' file is empty", resourcePath);
        }

        LOGGER.debug("'{}' file content => {}", resourcePath, resourceContentWithPlaceholders);

        String resourceContentWithPlaceholdersUpdated =
                resourceContentWithPlaceholders.replace("<regionId>", regionId);

        LOGGER.debug(
                "'{}' file content after <regionId> placeholders replacement => {}",
                resourcePath,
                resourceContentWithPlaceholdersUpdated);

        return resourceContentWithPlaceholdersUpdated;
    }

    private void initPlaceholderValues() {
        placeholderValues = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>("regionId", regionId),
                new AbstractMap.SimpleImmutableEntry<>("regionName", regionName))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String substituteRegionalPlaceholders(
            String strContainingPlaceholders,
            Map<String, String> placeholderValues) {
        return substituteRegionalPlaceholders(
                strContainingPlaceholders,
                placeholderValues,
                "<",
                ">");
    }

    private String substituteRegionalPlaceholders(
            String strContainingPlaceholders,
            Map<String, String> placeholderValues,
            String prefix,
            String suffix) {
        StrSubstitutor strSubstitutor = new StrSubstitutor(placeholderValues, prefix, suffix);
        return strSubstitutor.replace(strContainingPlaceholders);
    }
}