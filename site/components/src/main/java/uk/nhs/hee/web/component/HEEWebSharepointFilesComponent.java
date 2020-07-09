package uk.nhs.hee.web.component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;

import uk.nhs.hee.web.beans.opengraph.list.FileItem;
import uk.nhs.hee.web.ms.graph.service.GraphServiceFactory;

public class HEEWebSharepointFilesComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(HEEWebSharepointFilesComponent.class.getName());

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        setComponentId(request, response);

        try {
            GraphServiceFactory graphServiceFactory = new GraphServiceFactory(request);

            // Member Of
            Map<String, String> groups = graphServiceFactory.getGroupService().getAllGroups();

            // Removes main/domain group 'manifestouk' (for brevity)
            groups = groups
                    .entrySet()
                    .stream()
                    .filter(group -> !"manifestouk".equals(group.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            LOGGER.info("groups = " + groups);

            // Get root/group sites
            Map<String, String> sites = graphServiceFactory.getSiteService()
                    .getSitesByGroupIncludingRootSite(groups.keySet().stream().collect(Collectors.toList()));

            // Get sites with files
            Map<String, List<FileItem>> sharepointSiteFiles =
                    graphServiceFactory.getListItemService().getSharedFileItemsBySites(sites);

            LOGGER.debug("Sharepoint Site Files for the user '{}' => {}", request.getUserPrincipal().getName(), sites);

            request.setModel("sharepointSiteFiles", sharepointSiteFiles);
        } catch (AccessTokenRequiredException e) {
            LOGGER.error("Looks like 'accessToken' is not availabe in the session. "
                    + "Probably user didn't login yet. "
                    + "Wondering how the user has reached this point past spring security guard", e);
        } catch (ResourceException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
