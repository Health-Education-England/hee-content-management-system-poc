package uk.nhs.hee.web.ms.graph.service;

import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import uk.nhs.hee.web.ms.graph.service.util.GraphServiceBrokerUtil;

import java.util.*;

public class SiteService extends AbstractGraphService {

    protected SiteService(GraphServiceBrokerUtil graphServiceBrokerUtil) {
        super(graphServiceBrokerUtil);
    }

    public Map<String, String> getSitesByGroupIncludingRootSite(List<String> groupIds) throws ResourceException {
        Map<String, String> sites = new HashMap<>();
        Resource rootSiteResource =
                getGraphServiceBrokerUtil().getResources("/sites/root", Arrays.asList("id", "displayName"));

        sites.put(rootSiteResource.getValue("id").toString(), rootSiteResource.getValue("displayName").toString());

        // Get other group sites to which the user is a member of i.e. get the root site corresponding to the group to which the user is a member of
        groupIds.forEach(groupId -> {
            Resource groupSiteResource = getGraphServiceBrokerUtil().getResources(
                    "/groups/{groupId}/sites/root",
                    Collections.<String, Object>singletonMap("groupId", groupId),
                    Arrays.asList("id", "displayName"));

            sites.put(groupSiteResource.getValue("id").toString(), groupSiteResource.getValue("displayName").toString());
        });

        return sites;
    }

}
