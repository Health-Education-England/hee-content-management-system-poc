package uk.nhs.hee.web.ms.graph.service;

import uk.nhs.hee.web.ms.graph.service.util.GraphServiceBrokerUtil;

public class AbstractGraphService {
    private final GraphServiceBrokerUtil graphServiceBrokerUtil;

    protected AbstractGraphService(GraphServiceBrokerUtil graphServiceBrokerUtil) {
        this.graphServiceBrokerUtil = graphServiceBrokerUtil;
    }

    public GraphServiceBrokerUtil getGraphServiceBrokerUtil() {
        return graphServiceBrokerUtil;
    }
}
