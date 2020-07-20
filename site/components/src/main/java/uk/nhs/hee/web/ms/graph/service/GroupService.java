package uk.nhs.hee.web.ms.graph.service;

import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import uk.nhs.hee.web.ms.graph.service.util.GraphServiceBrokerUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GroupService extends AbstractGraphService {

    protected GroupService(GraphServiceBrokerUtil graphServiceBrokerUtil) {
        super(graphServiceBrokerUtil);
    }

    public Map<String, String> getAllGroups() throws ResourceException {
        Map<String, String> groups = new HashMap<>();
        Resource memberOfResource =
                getGraphServiceBrokerUtil().getResources("/me/memberOf", Arrays.asList("id", "displayName"));
        Resource memberOfResourceValues = (Resource) memberOfResource.getValue("value");

        memberOfResourceValues.getChildren().forEach(memberOfResourceValue -> {
            groups.put(memberOfResourceValue.getValue("id").toString(),
                    memberOfResourceValue.getValue("displayName").toString());
        });

        return groups;
    }

}
