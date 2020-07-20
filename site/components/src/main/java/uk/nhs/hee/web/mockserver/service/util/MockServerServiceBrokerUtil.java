package uk.nhs.hee.web.mockserver.service.util;


import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceBeanMapper;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.web.mockserver.service.bean.Programme;

public class MockServerServiceBrokerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockServerServiceBrokerUtil.class);

    private final ResourceServiceBroker resourceServiceBroker;
    private final String resourceSpace;
    private final ResourceBeanMapper resourceBeanMapper;

    public MockServerServiceBrokerUtil(ResourceServiceBroker resourceServiceBroker,
                                       String resourceSpace) {
        this.resourceServiceBroker = resourceServiceBroker;
        this.resourceSpace = resourceSpace;
        resourceBeanMapper = resourceServiceBroker.getResourceBeanMapper(resourceSpace);
    }

    public Resource getResource(String baseAbsPath) throws ResourceException {
        Resource resource = resourceServiceBroker.findResources(
                resourceSpace,
                baseAbsPath);

        LOGGER.debug("path = {}, data = {}", baseAbsPath, resource.getNodeData().toString());

        return resource;
    }

    public Programme getProgramme(String baseAbsPath) throws ResourceException {
        Resource resource = resourceServiceBroker.findResources(
                resourceSpace,
                baseAbsPath);

        LOGGER.debug("path = {}, data = {}", baseAbsPath, resource.getNodeData().toString());

        if (!resource.isAnyChildContained()) {
            return null;
        }

        return resourceBeanMapper.map(resource, Programme.class);
    }
}
