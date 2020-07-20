package uk.nhs.hee.web.ms.graph.service;

import org.onehippo.cms7.crisp.api.resource.ResourceException;
import uk.nhs.hee.web.ms.graph.service.util.GraphServiceBrokerUtil;

import java.util.List;
import java.util.Map;

public class UserService extends AbstractGraphService {

    protected UserService(GraphServiceBrokerUtil graphServiceBrokerUtil) {
        super(graphServiceBrokerUtil);
    }

    public Map<String, String> getUserProperties(List<String> properties) throws ResourceException {
        return getGraphServiceBrokerUtil().getResourcesAsMap("/me", properties);
    }

}
