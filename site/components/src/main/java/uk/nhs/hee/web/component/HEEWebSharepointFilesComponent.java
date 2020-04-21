package uk.nhs.hee.web.component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.nhs.hee.web.beans.opengraph.list.FileItem;
import uk.nhs.hee.web.component.info.HEEWebSharepointFilesComponentInfo;

@ParametersInfo(type = HEEWebSharepointFilesComponentInfo.class)
public class HEEWebSharepointFilesComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(HEEWebSharepointFilesComponent.class.getName());

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        setComponentId(request, response);

        try {
            ResourceServiceBroker broker = CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
            final HEEWebSharepointFilesComponentInfo paramInfo = getComponentParametersInfo(request);

            // Gets Id of the Site as indicated in the component, otherwise gets Id of 'TestSite'
            final String siteName = paramInfo.getSharepointSite();
            final String relativeSitePath =
                ("root".equals(siteName) || StringUtils.isEmpty(paramInfo.getSharepointSite())) ? "/" : "/sites/" + siteName;
            Resource sites = broker.findResources("openGraphResources", "/sites/manifestouk.sharepoint.com:" + relativeSitePath);

            /* final String siteId = paramInfo.getSharepointSite();
            Resource sites = broker.findResources("openGraphResources", "/sites/" + (StringUtils.isEmpty(siteId) ? "root" : siteId)); */

            final String siteId = (String) sites.getValueMap().get("id");
            LOGGER.error("Id of site '{}' => {}", siteName, siteId);

            if (StringUtils.isEmpty(siteId)) {
                LOGGER.error("Error in getting Id of the site => {}", siteName);
                return;
            }

            /* final String siteName = (String) sites.getValue("displayName");
            LOGGER.error("Site name => {}", siteName);

            if (StringUtils.isEmpty(siteName)) {
                LOGGER.error("Site with Id => {} doesn't exists", siteId);
                return;
            } */

            // Adds site display name to the request
            request.setAttribute("siteDisplayName", (String) sites.getValueMap().get("displayName"));

            // Gets Id of the 'Shared Documents' Id
            Map<String, Object> pathVars = new HashMap<>();
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

            request.setAttribute("fileItems", fileItems);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

}
