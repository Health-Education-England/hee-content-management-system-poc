package uk.nhs.hee.web.ms.graph.service;

import uk.nhs.hee.web.ms.graph.service.util.ResourceServiceBrokerUtil;

public class AbstractGraphService {
    private final ResourceServiceBrokerUtil resourceServiceBrokerUtil;

    protected AbstractGraphService(ResourceServiceBrokerUtil resourceServiceBrokerUtil) {
        this.resourceServiceBrokerUtil = resourceServiceBrokerUtil;
    }

    public ResourceServiceBrokerUtil getResourceServiceBrokerUtil() {
        return resourceServiceBrokerUtil;
    }
}
