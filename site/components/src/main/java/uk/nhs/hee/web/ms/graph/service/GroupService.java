package uk.nhs.hee.web.ms.graph.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;

import uk.nhs.hee.web.ms.graph.service.util.ResourceServiceBrokerUtil;

public class GroupService extends AbstractGraphService {

    protected GroupService(ResourceServiceBrokerUtil resourceServiceBrokerUtil) {
        super(resourceServiceBrokerUtil);
    }

    public Map<String, String> getAllGroups() throws ResourceException {
        final Map<String, String> groups = new HashMap<>();
        final Resource memberOfResource =
                getResourceServiceBrokerUtil().getResources("/me/memberOf", Arrays.asList("id", "displayName"));
        Resource memberOfResourceValues = (Resource) memberOfResource.getValue("value");

        memberOfResourceValues.getChildren().forEach(memberOfResourceValue -> {
            groups.put(memberOfResourceValue.getValue("id").toString(),
                    memberOfResourceValue.getValue("displayName").toString());
        });

        return groups;
    }

}
