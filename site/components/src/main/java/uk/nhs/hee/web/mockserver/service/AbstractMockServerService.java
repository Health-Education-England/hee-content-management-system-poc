package uk.nhs.hee.web.mockserver.service;


import uk.nhs.hee.web.mockserver.service.util.MockServerServiceBrokerUtil;

public class AbstractMockServerService {
    private final MockServerServiceBrokerUtil resourceServiceBrokerUtil;

    protected AbstractMockServerService(MockServerServiceBrokerUtil resourceServiceBrokerUtil) {
        this.resourceServiceBrokerUtil = resourceServiceBrokerUtil;
    }

    public MockServerServiceBrokerUtil getResourceServiceBrokerUtil() {
        return resourceServiceBrokerUtil;
    }
}
