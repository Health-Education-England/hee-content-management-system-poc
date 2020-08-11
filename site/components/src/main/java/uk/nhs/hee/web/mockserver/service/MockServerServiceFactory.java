package uk.nhs.hee.web.mockserver.service;

import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import uk.nhs.hee.web.mockserver.service.util.MockServerServiceBrokerUtil;

public class MockServerServiceFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MockServerServiceFactory.class);

    private final MockServerServiceBrokerUtil resourceServiceBrokerUtil;

    private ProgrammeService programmeService;

    public MockServerServiceFactory() throws AccessTokenRequiredException {
        resourceServiceBrokerUtil =
                new MockServerServiceBrokerUtil(
                        CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager()),
                        "mockServerResources");
    }

    public ProgrammeService getProgrammeService() {
        if (programmeService == null) {
            programmeService = new ProgrammeService(resourceServiceBrokerUtil);
        }

        return programmeService;
    }
}
