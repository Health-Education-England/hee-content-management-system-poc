package uk.nhs.hee.web.ms.graph.service;

import java.util.List;
import java.util.Map;

import org.onehippo.cms7.crisp.api.resource.ResourceException;

import uk.nhs.hee.web.ms.graph.service.util.ResourceServiceBrokerUtil;

public class UserService extends AbstractGraphService {

    protected UserService(ResourceServiceBrokerUtil resourceServiceBrokerUtil) {
        super(resourceServiceBrokerUtil);
    }

    public Map<String, String> getUserProperties(List<String> properties) throws ResourceException {
        return getResourceServiceBrokerUtil().getResourcesAsMap("/me", properties);
    }

}
