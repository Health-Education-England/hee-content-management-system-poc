package uk.nhs.hee.web.ms.graph.service;

import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.exchange.ExchangeHintBuilder;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;

import uk.nhs.hee.web.ms.graph.service.util.ResourceServiceBrokerUtil;

public class GraphServiceFactory {
    private final ResourceServiceBrokerUtil resourceServiceBrokerUtil;

    private UserService userService;
    private GroupService groupService;
    private SiteService siteService;
    private ListItemService listItemService;

    public GraphServiceFactory(String accessToken) {
        resourceServiceBrokerUtil =
                new ResourceServiceBrokerUtil(
                        CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager()),
                        "openGraphResources",
                        ExchangeHintBuilder
                                .create()
                                .requestHeader("Authorization", "Bearer " + accessToken)
                                .build());
    }

    public UserService getUserService() {
        if (userService == null) {
            userService = new UserService(resourceServiceBrokerUtil);
        }

        return userService;
    }

    public GroupService getGroupService() {
        if (groupService == null) {
            groupService = new GroupService(resourceServiceBrokerUtil);
        }

        return groupService;
    }

    public SiteService getSiteService() {
        if (siteService == null) {
            siteService = new SiteService(resourceServiceBrokerUtil);
        }

        return siteService;
    }

    public ListItemService getListItemService() {
        if (listItemService == null) {
            listItemService = new ListItemService(resourceServiceBrokerUtil);
        }

        return listItemService;
    }
}
