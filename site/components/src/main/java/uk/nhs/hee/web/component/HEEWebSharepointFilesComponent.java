package uk.nhs.hee.web.component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.exchange.ExchangeHint;
import org.onehippo.cms7.crisp.api.exchange.ExchangeHintBuilder;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import uk.nhs.hee.web.beans.opengraph.list.FileItem;
import uk.nhs.hee.web.beans.opengraph.list.User;
import uk.nhs.hee.web.component.info.HEEWebSharepointFilesComponentInfo;

@ParametersInfo(type = HEEWebSharepointFilesComponentInfo.class)
public class HEEWebSharepointFilesComponent extends CommonComponent {

    private static final Logger LOG = LoggerFactory.getLogger(HEEWebSharepointFilesComponent.class.getName());

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        setComponentId(request, response);

        try {
            final String username = request.getUserPrincipal().getName();
            System.out.println("username -> " + username);

            /* OAuth2Authentication authentication =
                    (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null) {
                LOG.error("Authentication Details (OAuth2Authentication) aren't available " +
                        "in the Spring SecurityContext");
                return;
            } */

            // Get the current bloomreach site user session
            final String accessToken = request.getSession().getAttribute("accessToken").toString();
            // final String accessToken = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
            System.out.println("accessToken = " + accessToken);
            LOG.info("accessToken = {}", accessToken);

            ResourceServiceBroker broker =
                    CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());

            if (StringUtils.isEmpty(accessToken)) {
                LOG.info("'accessToken' is not availabe in the session");
                return;
            }

            // Inject Azure AD auth_token corresponding to the user logged in bloomreach site
            final ExchangeHint exchangeHint = ExchangeHintBuilder
                .create()
                .requestHeader("Authorization", "Bearer " + accessToken)
                .build();

            // Profile
            final Resource me =
                    broker.findResources("openGraphResources", "/me", exchangeHint);
            User user = new User();
            user.setUsername(me.getValue("userPrincipalName").toString());
            user.setDisplayName(me.getValue("displayName").toString());
            user.setJobTitle(me.getValue("jobTitle").toString());

            // Member Of
            final Resource memberOf =
                    broker.findResources("openGraphResources", "/me/memberOf", exchangeHint);
            final Resource memberOfValues = (Resource) memberOf.getValue("value");

            final List<String> groups = new ArrayList<>();
            final List<String> groupIds = new ArrayList<>();
            for (Resource memberOfValue : memberOfValues.getChildren()) {
                if ("manifestouk".equals(memberOfValue.getValue("displayName").toString())) {
                    // Main/domain group
                    continue;
                }

                groups.add(memberOfValue.getValue("displayName").toString());
                groupIds.add(memberOfValue.getValue("id").toString());
            }
            user.setGroups(groups);

            // Get main/root Sharepoint site
            final Map<String, String> siteIdNames = new HashMap<>();
            final Resource rootSite =
                    broker.findResources("openGraphResources", "/sites/root", exchangeHint);
            siteIdNames.put(rootSite.getValue("id").toString(), rootSite.getValue("displayName").toString());

            // Get other group sites to which the user is a member of i.e. get the root site corresponding to the group to which the user is a member of
            Map<String, Object> pathVars = null;
            for (final String groupId : groupIds) {
                pathVars = new HashMap<>();
                pathVars.put("groupId", groupId);
                final Resource groupSites =
                        broker.findResources("openGraphResources",
                                "/groups/{groupId}/sites/root", pathVars, exchangeHint);
                siteIdNames.put(groupSites.getValue("id").toString(), groupSites.getValue("displayName").toString());
            }

            // Get files from root & other group sites to which the user is a member of (actually, same old logic)
            final Map<String, List<FileItem>> siteFiles = new HashMap<>();
            for (final Entry<String, String> site : siteIdNames.entrySet()) {
                final String siteId = site.getKey();

                // Gets Id of the 'Shared Documents' Id
                pathVars = new HashMap<>();
                pathVars.put("siteId", siteId);
                Resource lists = broker.findResources("openGraphResources", "/sites/{siteId}/lists", pathVars);
                Resource listValues = (Resource) lists.getValueMap().get("value");

                String sharedDocumentsListId = StringUtils.EMPTY;
                for (Resource listValue : listValues.getChildren()) {
                    final String listValueName = (String) listValue.getValueMap().get("name");

                    if ("Shared Documents".equals(listValueName)) {
                        sharedDocumentsListId = (String) listValue.getValueMap().get("id");
                        LOG.debug("Found 'Shared Documents' list with Id => {}", sharedDocumentsListId);
                        break;
                    }
                }

                if (StringUtils.isEmpty(sharedDocumentsListId)) {
                    LOG.error("'Shared Documents' list is not found");
                    return;
                }

                // Gets Items (i.e. files) listed under 'Shared Documents' and adds it to request as List<FileItem>
                pathVars = new HashMap<>();
                pathVars.put("siteId", siteId);
                pathVars.put("listId", sharedDocumentsListId);
                Resource listItems = broker.findResources("openGraphResources",
                        "/sites/{siteId}/lists/{listId}/items?expand=fields", pathVars);
                Resource listItemValues = (Resource) listItems.getValueMap().get("value");

                List<FileItem> fileItems = new ArrayList<>();
                for (Resource listItemValue : listItemValues.getChildren()) {
                    // dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    fileItems.add(
                        new FileItem(
                            (String) listItemValue.getValue("fields/LinkFilename"),
                            (String) listItemValue.getValue("webUrl"),
                            (String) listItemValue.getValue("fields/DocIcon"),
                            (String) listItemValue.getValue("lastModifiedBy/user/displayName"),
                            DATE_FORMAT.parse((String) listItemValue.getValue("lastModifiedDateTime"))
                        )
                    );
                }

                siteFiles.put(site.getValue(), fileItems);
            }

            user.setSiteFiles(siteFiles);

            request.setAttribute("user", user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
