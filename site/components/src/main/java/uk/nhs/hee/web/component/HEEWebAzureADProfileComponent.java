package uk.nhs.hee.web.component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;

import uk.nhs.hee.web.beans.opengraph.list.User;
import uk.nhs.hee.web.ms.graph.service.GraphServiceFactory;

public class HEEWebAzureADProfileComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(HEEWebAzureADProfileComponent.class.getName());

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        setComponentId(request, response);

        try {
            GraphServiceFactory graphServiceFactory = new GraphServiceFactory(request);

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

            LOGGER.debug("User AD Profile = {}", user);

            request.setModel("user", user);
        } catch (AccessTokenRequiredException e) {
            LOGGER.error("Looks like 'accessToken' is not availabe in the session. "
                    + "Probably user didn't login yet. "
                    + "Wondering how the user has reached this point past spring security guard", e);
        } catch (ResourceException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
