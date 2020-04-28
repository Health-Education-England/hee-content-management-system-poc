package uk.nhs.hee.web.component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import uk.nhs.hee.web.beans.opengraph.list.FileItem;
import uk.nhs.hee.web.beans.opengraph.list.User;
import uk.nhs.hee.web.ms.graph.service.GraphServiceFactory;

public class HEEWebSharepointFilesComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(HEEWebSharepointFilesComponent.class.getName());

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        setComponentId(request, response);

        String username = request.getUserPrincipal().getName();
        LOGGER.debug("username = {}", username);

        String accessToken = getAccessToken(request);
        LOGGER.debug("accessToken = {}", accessToken);

        if (StringUtils.isEmpty(accessToken)) {
            LOGGER.info("'accessToken' is not availabe in the session. "
                    + "Probably user didn't login yet. "
                    + "Wondering how the user has reached this point past spring security guard");
            return;
        }

        GraphServiceFactory graphServiceFactory = new GraphServiceFactory(accessToken);

        try {
            // Profile
            Map<String, String> userProperties = graphServiceFactory.getUserService().getUserProperties(
                    Arrays.asList("userPrincipalName", "displayName", "jobTitle"));

            User user = new User();
            user.setUsername(userProperties.get("userPrincipalName"));
            user.setDisplayName(userProperties.get("displayName"));
            user.setJobTitle(userProperties.get("jobTitle"));

            // Member Of
            Map<String, String> groups = graphServiceFactory.getGroupService().getAllGroups();

            // Removes main/domain group 'manifestouk' (for brevity)
            groups = groups
                    .entrySet()
                    .stream()
                    .filter(group -> !"manifestouk".equals(group.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            LOGGER.info("groups = " + groups);

            user.setGroups(groups.values().stream().collect(Collectors.toList()));

            // Get root/group sites
            Map<String, String> sites = graphServiceFactory.getSiteService()
                    .getSitesByGroupIncludingRootSite(groups.keySet().stream().collect(Collectors.toList()));

            // Get sites with files
            Map<String, List<FileItem>> sitesFiles =
                    graphServiceFactory.getListItemService().getSharedFileItemsBySites(sites);
            user.setSiteFiles(sitesFiles);

            LOGGER.debug("User Details = {}", user);

            request.setAttribute("user", user);
        } catch (ResourceException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Returns logged in user's <code>Access Token</code> from
     * {@link org.springframework.security.core.context.SecurityContext}
     *
     * @param request the {@link HstRequest} instance.
     * @return the logged in user's <code>Access Token</code> from
     * {@link org.springframework.security.core.context.SecurityContext}
     */
    private String getAccessToken(HstRequest request) {
        OAuth2Authentication authentication =
                (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            LOGGER.error("Authentication Details (OAuth2Authentication) aren't available " +
                    "in the Spring SecurityContext");
            return null;
        }

        return ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
    }

}
