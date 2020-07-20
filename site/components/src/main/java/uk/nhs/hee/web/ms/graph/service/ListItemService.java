package uk.nhs.hee.web.ms.graph.service;

import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.web.beans.opengraph.list.FileItem;
import uk.nhs.hee.web.ms.graph.service.util.GraphServiceBrokerUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListItemService extends AbstractGraphService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListItemService.class);


    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    protected ListItemService(GraphServiceBrokerUtil graphServiceBrokerUtil) {
        super(graphServiceBrokerUtil);
    }

    public Map<String, List<FileItem>> getSharedFileItemsBySites(Map<String, String> sites)
            throws ResourceException {
        Map<String, List<FileItem>> sitesFiles = new HashMap<>();

        sites.forEach((siteId, siteDisplayName) -> {
            String documentListId = getDocumentsListId(siteId);

            if (documentListId != null) {
                sitesFiles.put(siteDisplayName, getFileItems(siteId, documentListId));
            }
        });

        return sitesFiles;
    }

    private String getDocumentsListId(String siteId) {
        // TODO: Ideally, the 'Documents' ListId should be filtered in the call
        /* final Resource listResource = getResourceServiceBrokerUtil().getResources(
                "/sites/{siteId}/lists",
                Collections.<String, Object>singletonMap("siteId", siteId),
                Collections.singletonList("id"),
                "displayName eq 'Documents'"); */

        Resource listResource = getGraphServiceBrokerUtil().getResources(
                "/sites/{siteId}/lists",
                Collections.<String, Object>singletonMap("siteId", siteId),
                Arrays.asList("id", "displayName"));

        Resource listValueResource = ((Resource) listResource.getValueMap().get("value"))
                .getChildren()
                .getCollection()
                .stream()
                .filter(list -> "Documents".equals(list.getValue("displayName")))
                .findFirst()
                .orElse(null);

        if (listValueResource == null) {
            return null;
        }

        return listValueResource.getValue("id").toString();
    }

    private List<FileItem> getFileItems(String siteId, String documentListId) {
        Map<String, Object> pathVariables = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>("siteId", siteId),
                new AbstractMap.SimpleImmutableEntry<>("listId", documentListId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Resource listItemResource = getGraphServiceBrokerUtil().getResources(
                "/sites/{siteId}/lists/{listId}/items?expand=fields",
                pathVariables);

        /* Resource listItemValues = (Resource) listItemResource.getValueMap().get("value");
        for (Resource listItemValue : listItemValues.getChildren()) {
            if (!"Folder".equals(((String) listItemValue.getValue("fields/ContentType")))) {
                fileItems.add(
                        new FileItem(
                            (String) listItemValue.getValue("fields/LinkFilename"),
                            (String) listItemValue.getValue("webUrl"),
                            (String) listItemValue.getValue("fields/DocIcon"),
                            (String) listItemValue.getValue("lastModifiedBy/user/displayName"),
                            getFormattedDate(listItemValue.getValue("lastModifiedDateTime").toString())
                        )
                    );
            }
        } */

        List<FileItem> fileItems = new ArrayList<>();

        ((Resource) listItemResource.getValueMap().get("value")).getChildren().forEach(listItemValue -> {
            if (!"Folder".equals(((String) listItemValue.getValue("fields/ContentType")))) {
                fileItems.add(
                        new FileItem(
                                (String) listItemValue.getValue("fields/LinkFilename"),
                                (String) listItemValue.getValue("webUrl"),
                                (String) listItemValue.getValue("fields/DocIcon"),
                                (String) listItemValue.getValue("lastModifiedBy/user/displayName"),
                                getFormattedDate(listItemValue.getValue("lastModifiedDateTime").toString())
                        )
                );
            }
        });

        return fileItems;
    }

    private Date getFormattedDate(String dateString) {
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

}
