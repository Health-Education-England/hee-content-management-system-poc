package uk.nhs.hee.web.ms.graph.service.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.exchange.ExchangeHint;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceServiceBrokerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServiceBrokerUtil.class);


    private final ResourceServiceBroker resourceServiceBroker;
    private final String resourceSpace;
    private final ExchangeHint exchangeHint;

    public ResourceServiceBrokerUtil(ResourceServiceBroker resourceServiceBroker,
                                     String resourceSpace, ExchangeHint exchangeHint) {
        this.resourceServiceBroker = resourceServiceBroker;
        this.resourceSpace = resourceSpace;
        this.exchangeHint = exchangeHint;
    }

    public Resource getResources(
            final String baseAbsPath,
            final Map<String, Object> pathVariables,
            final List<String> selectProperties) throws ResourceException {

        return getResources(baseAbsPath + "?" + buildSelectParameter(selectProperties), pathVariables);
    }

    public Resource getResources(
            final String baseAbsPath,
            final Map<String, Object> pathVariables,
            final List<String> selectProperties,
            final String filterCondition) throws ResourceException {

        return getResources(
                baseAbsPath + "?" + buildSelectParameter(selectProperties) + "&$filter=" + filterCondition,
                pathVariables);
    }

    public Resource getResources(
            final String baseAbsPath,
            final Map<String, Object> pathVariables) throws ResourceException {

        final Resource resource = resourceServiceBroker.findResources(
                resourceSpace,
                baseAbsPath,
                pathVariables,
                exchangeHint);

        LOGGER.debug("path = {}, pathVariables = {}, data = {}",
                baseAbsPath, pathVariables, resource.getNodeData().toString());

        return resource;
    }

    public Map<String, String> getResourcesAsMap(
            final String baseAbsPath,
            final List<String> selectProperties) throws ResourceException {

        Map<String, String> resourceMap = new HashMap<>();
        Resource resource = getResources(baseAbsPath, selectProperties);

        selectProperties.forEach(property -> {
            resourceMap.put(property, resource.getValue(property).toString());
        });

        return resourceMap;
    }

    public Resource getResources(
            final String baseAbsPath,
            final List<String> selectProperties) throws ResourceException {

        final String baseAbsPathWithSelectProperties = baseAbsPath + "?" + buildSelectParameter(selectProperties);

        final Resource resource = resourceServiceBroker.findResources(
                resourceSpace,
                baseAbsPathWithSelectProperties,
                exchangeHint);

        LOGGER.debug("path = {}, data = {}", baseAbsPathWithSelectProperties, resource.getNodeData().toString());

        return resource;
    }

    private String buildSelectParameter(final List<String> selectProperties) {
        return "$select=" + StringUtils.join(selectProperties, ',');
    }
}
