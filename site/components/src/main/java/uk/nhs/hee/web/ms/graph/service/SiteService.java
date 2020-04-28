package uk.nhs.hee.web.ms.graph.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;

import uk.nhs.hee.web.ms.graph.service.util.ResourceServiceBrokerUtil;

public class SiteService extends AbstractGraphService {

    protected SiteService(ResourceServiceBrokerUtil resourceServiceBrokerUtil) {
        super(resourceServiceBrokerUtil);
    }

    public Map<String, String> getSitesByGroupIncludingRootSite(final List<String> groupIds) throws ResourceException {
        final Map<String, String> sites = new HashMap<>();
        final Resource rootSiteResource =
                getResourceServiceBrokerUtil().getResources("/sites/root", Arrays.asList("id", "displayName"));

        sites.put(rootSiteResource.getValue("id").toString(), rootSiteResource.getValue("displayName").toString());

        // Get other group sites to which the user is a member of i.e. get the root site corresponding to the group to which the user is a member of
        groupIds.forEach(groupId -> {
            final Resource groupSiteResource = getResourceServiceBrokerUtil().getResources(
                    "/groups/{groupId}/sites/root",
                    Collections.<String, Object>singletonMap("groupId", groupId),
                    Arrays.asList("id", "displayName"));

            sites.put(groupSiteResource.getValue("id").toString(), groupSiteResource.getValue("displayName").toString());
        });

        return sites;
    }

}
