package uk.nhs.hee.web.ms.graph.service;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.exchange.ExchangeHintBuilder;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import uk.nhs.hee.web.ms.graph.service.util.ResourceServiceBrokerUtil;

public class GraphServiceFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphServiceFactory.class);

    private final ResourceServiceBrokerUtil resourceServiceBrokerUtil;

    private UserService userService;
    private GroupService groupService;
    private SiteService siteService;
    private ListItemService listItemService;

    public GraphServiceFactory(final HstRequest request) throws AccessTokenRequiredException {
        final String accessToken = getAccessToken(request);

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

    /**
     * Returns logged in user's <code>Access Token</code> from
     * {@link org.springframework.security.core.context.SecurityContext}
     *
     * @param request the {@link HstRequest} instance.
     * @return the logged in user's <code>Access Token</code> from
     * {@link org.springframework.security.core.context.SecurityContext}
     */
    private String getAccessToken(HstRequest request) throws AccessTokenRequiredException {
        OAuth2Authentication authentication =
                (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            final String errorMsg = "Authentication Details (OAuth2Authentication) aren't available " +
                    "in the Spring SecurityContext";
            LOGGER.error(errorMsg);
            throw new AccessTokenRequiredException(errorMsg, null);
        }

        return ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
    }
}
