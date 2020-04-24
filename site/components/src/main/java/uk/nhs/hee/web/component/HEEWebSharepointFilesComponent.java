package uk.nhs.hee.web.component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import uk.nhs.hee.web.beans.opengraph.list.FileItem;
import uk.nhs.hee.web.beans.opengraph.list.User;
import uk.nhs.hee.web.component.info.HEEWebSharepointFilesComponentInfo;

@ParametersInfo(type = HEEWebSharepointFilesComponentInfo.class)
public class HEEWebSharepointFilesComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(HEEWebSharepointFilesComponent.class.getName());

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

     private static final Map<String, String> repoUserAccessTokenMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("jrees", "eyJ0eXAiOiJKV1QiLCJub25jZSI6IjFnTWp5aV9TV0hKaGgwMkpyWjZ2ODQzZlR4ZkZtX2NuZE9KRXZYNnRKQTAiLCJhbGciOiJSUzI1NiIsIng1dCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSIsImtpZCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC85YTA2MzU3Zi0xMGI3LTRhMjYtYmUwNS0xNTZmMjdlZTBiZDIvIiwiaWF0IjoxNTg3NTY1MDAxLCJuYmYiOjE1ODc1NjUwMDEsImV4cCI6MTU4NzU2ODkwMSwiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhQQUFBQXhJSG1YVXZDQWlFQzVydmFveFZlazh0ZWxRNlpzdnhZTWFycTJjOWt1Qzg9IiwiYW1yIjpbInB3ZCJdLCJhcHBfZGlzcGxheW5hbWUiOiJwb3N0bWFuIiwiYXBwaWQiOiI2YWY5ODk0Mi01N2VjLTRjYzAtYTUwZi1kMTViMGE1NDYzM2QiLCJhcHBpZGFjciI6IjEiLCJmYW1pbHlfbmFtZSI6IlJlZXMiLCJnaXZlbl9uYW1lIjoiSm9uYXRoYW4iLCJpcGFkZHIiOiIxNjIuMjE2LjE0MS45IiwibmFtZSI6IkpvbmF0aGFuIFJlZXMiLCJvaWQiOiIxYWM5ODBjZi0wM2I4LTQyNzYtYjkzNS03YTQ3MGMwZjUwYmYiLCJwbGF0ZiI6IjE0IiwicHVpZCI6IjEwMDMyMDAwQjVGRkY3RDMiLCJzY3AiOiJGaWxlcy5SZWFkIEZpbGVzLlJlYWQuQWxsIEZpbGVzLlJlYWQuU2VsZWN0ZWQgR3JvdXAuUmVhZC5BbGwgR3JvdXBNZW1iZXIuUmVhZC5BbGwgU2l0ZXMuUmVhZC5BbGwgVXNlci5SZWFkIHByb2ZpbGUgb3BlbmlkIGVtYWlsIiwic3ViIjoidktramYtWk0xV05OblYxd0tpWUpYTzJHSTFocU94S0t6X09GSVVQb0NGYyIsInRpZCI6IjlhMDYzNTdmLTEwYjctNGEyNi1iZTA1LTE1NmYyN2VlMGJkMiIsInVuaXF1ZV9uYW1lIjoianJlZXNAbWFuaWZlc3RvdWsub25taWNyb3NvZnQuY29tIiwidXBuIjoianJlZXNAbWFuaWZlc3RvdWsub25taWNyb3NvZnQuY29tIiwidXRpIjoibGhxbXl3bXg1a0tWcy1uOHN3VTdBQSIsInZlciI6IjEuMCIsInhtc19zdCI6eyJzdWIiOiJKeTkxQWUyWGJHckpJcm5ZalBjdDB2WFVpZ1BxX284UzlNdGNreC0yZng4In0sInhtc190Y2R0IjoxNTg3MDA3MTY5fQ.mDh_rq5qwFU9MHsxDM55VF23rj0MDpWybXYURlxbUTwy4zayZUkTh9Qhsi2FWLZQk_CK9pptmAI4BNciEA76iT8lb_I79GV6YDaFednxblKE6GLOu0bdPBgDTsjC5gCsAIs8RRIWdeQIMFJXXZePCF2eGIwhW1X2_3brXsoutQn-sYQOInZTAuFt9RIxTwl6KWfy_loC2FVNGhbnKsRx8bvLewQQle671Rn3BswE-8AQOznRJGigQMq25XEq-pR1L4pQMPwvP3c2n1sjyksVqFCP0jc_QTO0bylLjn1La18-xoFM1ao8_HdbICMQw_MqGdSM1_G_v_kHMmIcy8ezsA");
            put("mmann", "eyJ0eXAiOiJKV1QiLCJub25jZSI6IlM4bnU0eFJXYmRZb09vbjlmemFrYmFVRlkzU1NHdk5uWWtRdTJCeFBrWUkiLCJhbGciOiJSUzI1NiIsIng1dCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSIsImtpZCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC85YTA2MzU3Zi0xMGI3LTRhMjYtYmUwNS0xNTZmMjdlZTBiZDIvIiwiaWF0IjoxNTg3NTY1MDM2LCJuYmYiOjE1ODc1NjUwMzYsImV4cCI6MTU4NzU2ODkzNiwiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IjQyZGdZRmdvbS9VbmQ4c2xMb0ZKREx2M1h6cDdocDNkT0orMytxaUoybDhlVVcwZGpuMEEiLCJhbXIiOlsicHdkIl0sImFwcF9kaXNwbGF5bmFtZSI6InBvc3RtYW4iLCJhcHBpZCI6IjZhZjk4OTQyLTU3ZWMtNGNjMC1hNTBmLWQxNWIwYTU0NjMzZCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiTWFubiIsImdpdmVuX25hbWUiOiJNYXR0aGV3IiwiaXBhZGRyIjoiMTYyLjIxNi4xNDEuOSIsIm5hbWUiOiJNYXR0aGV3IE1hbm4iLCJvaWQiOiIzOGI4N2JlMC02MTVjLTQ5YWQtOWNjZS00NTk3ZTQ3OTk5ZWMiLCJwbGF0ZiI6IjE0IiwicHVpZCI6IjEwMDMyMDAwQjVGOTMwRkEiLCJzY3AiOiJGaWxlcy5SZWFkIEZpbGVzLlJlYWQuQWxsIEZpbGVzLlJlYWQuU2VsZWN0ZWQgR3JvdXAuUmVhZC5BbGwgR3JvdXBNZW1iZXIuUmVhZC5BbGwgU2l0ZXMuUmVhZC5BbGwgVXNlci5SZWFkIHByb2ZpbGUgb3BlbmlkIGVtYWlsIiwic3ViIjoiN1pBRGR1R2hGakV5aDZvd0hSQ1FXMlpsMzI3U3VSVFJpeXlhNEExLWVQSSIsInRpZCI6IjlhMDYzNTdmLTEwYjctNGEyNi1iZTA1LTE1NmYyN2VlMGJkMiIsInVuaXF1ZV9uYW1lIjoibW1hbm5AbWFuaWZlc3RvdWsub25taWNyb3NvZnQuY29tIiwidXBuIjoibW1hbm5AbWFuaWZlc3RvdWsub25taWNyb3NvZnQuY29tIiwidXRpIjoibGhxbXl3bXg1a0tWcy1uOFNBbzdBQSIsInZlciI6IjEuMCIsInhtc19zdCI6eyJzdWIiOiJ6d0hKZW5FRzFuUWttZ3lWU0Rtajh5cUFWNkdDOHM2NVM3TTVIRktfNE1JIn0sInhtc190Y2R0IjoxNTg3MDA3MTY5fQ.dtePVL0jqMOhHcrqoAptdjWmYKxuSayCSCKtOEtDFUwGuukSiZSK2qWu1orP2uUnOjkYfKnMiG3XlxETmQcEj2Pet2WltqocR_EfzVUYXUuVX7XR5VXSLso2ypjX9hi4TjpEMCDDHOl79Zezgm3PThdefS5G3MxPmmyBNQZhzQRQLWazyG2-GCtGNsFJpx2RxOThuLkWonLLhGKXt_PnsdDsE4lACwzPvyGBAUwnpIPdbT6uW52sHyFIaKiYelX-KTNEr1bi8XFQ9c8Er8xLKKeDrz_n2t08BPK10mExHmZ7-rf-e8AfQPp24t4FJLkk3MdV_MMFKj7D4bPOUU_hHw");
            put("epearson", "eyJ0eXAiOiJKV1QiLCJub25jZSI6Im56M1pJLUs1ODBPb2xGbVZEa2RSbEk5eUdLdmRVZV82Q3ozZ0tiSDZsbU0iLCJhbGciOiJSUzI1NiIsIng1dCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSIsImtpZCI6IllNRUxIVDBndmIwbXhvU0RvWWZvbWpxZmpZVSJ9.eyJhdWQiOiJodHRwczovL2dyYXBoLm1pY3Jvc29mdC5jb20iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC85YTA2MzU3Zi0xMGI3LTRhMjYtYmUwNS0xNTZmMjdlZTBiZDIvIiwiaWF0IjoxNTg3NTY1MDU4LCJuYmYiOjE1ODc1NjUwNTgsImV4cCI6MTU4NzU2ODk1OCwiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhQQUFBQXNwYnpqRVBrTzdVUitFUzQvYm9DcitveWlBczlxZE44a0dMZjhSSUNMR1E9IiwiYW1yIjpbInB3ZCJdLCJhcHBfZGlzcGxheW5hbWUiOiJwb3N0bWFuIiwiYXBwaWQiOiI2YWY5ODk0Mi01N2VjLTRjYzAtYTUwZi1kMTViMGE1NDYzM2QiLCJhcHBpZGFjciI6IjEiLCJmYW1pbHlfbmFtZSI6IlBlYXJzb24iLCJnaXZlbl9uYW1lIjoiRWxsaWUiLCJpcGFkZHIiOiIxNjIuMjE2LjE0MS45IiwibmFtZSI6IkVsbGllIFBlYXJzb24iLCJvaWQiOiIxYTE3MDBmNC1jMTBjLTQ1MjctOTk5Yy05OTY5NjFlNDA0ZjMiLCJwbGF0ZiI6IjE0IiwicHVpZCI6IjEwMDMyMDAwQjVGQzc1OTMiLCJzY3AiOiJGaWxlcy5SZWFkIEZpbGVzLlJlYWQuQWxsIEZpbGVzLlJlYWQuU2VsZWN0ZWQgR3JvdXAuUmVhZC5BbGwgR3JvdXBNZW1iZXIuUmVhZC5BbGwgU2l0ZXMuUmVhZC5BbGwgVXNlci5SZWFkIHByb2ZpbGUgb3BlbmlkIGVtYWlsIiwic3ViIjoiYmQ0Qm1kU1EyUTRfdktIWElvbC1wQkpaakd0MXpMY0QyUEJ5OUt3anZpQSIsInRpZCI6IjlhMDYzNTdmLTEwYjctNGEyNi1iZTA1LTE1NmYyN2VlMGJkMiIsInVuaXF1ZV9uYW1lIjoiZXBlYXJzb25AbWFuaWZlc3RvdWsub25taWNyb3NvZnQuY29tIiwidXBuIjoiZXBlYXJzb25AbWFuaWZlc3RvdWsub25taWNyb3NvZnQuY29tIiwidXRpIjoibGhVZXFodXRlMHljTWhNemtray1BQSIsInZlciI6IjEuMCIsInhtc19zdCI6eyJzdWIiOiJXVTZkWDZ0WjR3VHZYQWtJSEZIYWhjMWt3bmxLSEU4LUYyWnVLdHVrVmFRIn0sInhtc190Y2R0IjoxNTg3MDA3MTY5fQ.SMcI6CL1iYFFgT0YDeDlXWv86-05IE-OUR6MWqIDyMqgVr01AIz8RpajxLcs1aILNe2ouohVe15nU8t4TIGa6Mq6slqFlXhPhomzGZCzr6Pi6C53j5OjBCJ1ZIG5k4ahQa-jdzBwz9uB3p32pA-CbZVkE5GkPflz6IFtpLK7jxjkMGCiK-j7nYLd5swcc7BEeKHP3arL_9im1Q79ounSh00gKWjfoB2HmxgMHA0QKiiS7OXbpgFfWIYHtlKrq4utiMo1xMeCpFEAlpl2wi3oRlcxxnBRwrPPJthw-1gG5H6RV3AfxUWIK1MSHGVJlBk-L78wPhQe0qtM2IBeKvl-Aw");
         }
    };

    /* private static final Map<String, String> credentialMap = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("jrees", "*****");
            put("mmann", "*****");
            put("epearson", "*****");
         }
    }; */

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        System.out.println("In uk.nhs.hee.web.component.HEEWebSharepointFilesComponent.doBeforeRender(HstRequest, HstResponse)");
        super.doBeforeRender(request, response);

        setComponentId(request, response);

        try {
            ResourceServiceBroker broker = CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());

            /* Resource me = broker.findResources("openGraphResources", "/me");
            System.out.println("userPrincipalName -> " + me.getValue("userPrincipalName")); */

            // Get the current bloomreach site user session
            final String username = request.getUserPrincipal().getName();

            if (!repoUserAccessTokenMap.containsKey(username)) {
                LOGGER.info("User {} doesn't have an account on Azure AD", username);
                return;
            }

            /* if (!credentialMap.containsKey(username)) {
                LOGGER.info("User {} doesn't have an account on Azure AD", username);
                return;
            } */

            // Inject Azure AD auth_token corresponding to the user logged in bloomreach site
            final ExchangeHint exchangeHint = ExchangeHintBuilder
                .create()
                .requestHeader("Authorization", "Bearer " + repoUserAccessTokenMap.get(username))
                // .requestHeader("userName", username + "@manifestouk.onmicrosoft.com")
                // .requestHeader("password", credentialMap.get(username))
                .build();

            // Profile
            final Resource me = broker.findResources("openGraphResources", "/me", exchangeHint);
            User user = new User();
            user.setUsername(me.getValue("userPrincipalName").toString());
            user.setDisplayName(me.getValue("displayName").toString());
            user.setJobTitle(me.getValue("jobTitle").toString());

            // Member Of
            final Resource memberOf = broker.findResources("openGraphResources", "/me/memberOf", exchangeHint);
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
            final Resource rootSite = broker.findResources("openGraphResources", "/sites/root", exchangeHint);
            siteIdNames.put(rootSite.getValue("id").toString(), rootSite.getValue("displayName").toString());

            // Get other group sites to which the user is a member of i.e. get the root site corresponding to the group to which the user is a member of
            Map<String, Object> pathVars = null;
            for (final String groupId : groupIds) {
                pathVars = new HashMap<>();
                pathVars.put("groupId", groupId);
                final Resource groupSites = broker.findResources("openGraphResources", "/groups/{groupId}/sites/root", pathVars, exchangeHint);
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
                        LOGGER.debug("Found 'Shared Documents' list with Id => {}", sharedDocumentsListId);
                        break;
                    }
                }

                if (StringUtils.isEmpty(sharedDocumentsListId)) {
                    LOGGER.error("'Shared Documents' list is not found");
                    return;
                }

                // Gets Items (i.e. files) listed under 'Shared Documents' and adds it to request as List<FileItem>
                pathVars = new HashMap<>();
                pathVars.put("siteId", siteId);
                pathVars.put("listId", sharedDocumentsListId);
                Resource listItems = broker.findResources("openGraphResources", "/sites/{siteId}/lists/{listId}/items?expand=fields", pathVars);
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
                            dateFormat.parse((String) listItemValue.getValue("lastModifiedDateTime"))
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

        return;
    }

}
