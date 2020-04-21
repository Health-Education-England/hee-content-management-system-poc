package uk.nhs.hee.web.component.parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.parameters.ValueListProvider;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;

public class SharepointSiteValueListProvider implements ValueListProvider {

    private static final List<String> NON_USER_MANAGED_SITES = Arrays.asList("Team Site", "Selvandhan Manivasagam", "manifestouk");

    private static final Map<String, String> SHAREPOINT_SITES_MAP = new LinkedHashMap<>();
    private static List<String> SHAREPOINT_SITES_LIST = new ArrayList<>();

    static {
        SHAREPOINT_SITES_MAP.put("root", "HEE Communication Site"); // Root site
        SHAREPOINT_SITES_MAP.put("TestSite", "Test Site");

        SHAREPOINT_SITES_LIST = Collections.unmodifiableList(new LinkedList<>(SHAREPOINT_SITES_MAP.keySet()));
    }

    private void initSharepointSitesMap() {
        if (!SHAREPOINT_SITES_MAP.isEmpty()) {
            return;
        }

        ResourceServiceBroker broker = CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
        Resource sites = broker.findResources("openGraphResources", "/sites");
        Resource siteValues = (Resource) sites.getValue("value");

        for (Resource siteValue : siteValues.getChildren()) {
            String siteDisplayName = (String) siteValue.getValue("displayName");
            System.out.println("siteDisplayName -> " + siteDisplayName);

            if (StringUtils.isEmpty(siteDisplayName) || NON_USER_MANAGED_SITES.contains(siteDisplayName)) {
                continue;
            }
            SHAREPOINT_SITES_MAP.put((String) siteValue.getValue("id"), siteDisplayName);
        }

        /* CSS_DISPLAY_VALUES_MAP.put("root", "HEE Communication Site"); // Root site
        CSS_DISPLAY_VALUES_MAP.put("TestSite", "Test Site"); */

        // CSS_DISPLAY_VALUES_LIST = Collections.unmodifiableList(new LinkedList<>(SHAREPOINT_SITES_MAP.keySet()));
    }

    @Override
    public List<String> getValues() {
        return SHAREPOINT_SITES_LIST;
    }

    @Override
    public String getDisplayValue(final String value) {
        return getDisplayValue(value, null);
    }

    @Override
    public String getDisplayValue(final String value, final Locale locale) {
        final String displayValue = SHAREPOINT_SITES_MAP.get(value);
        return (displayValue != null) ? displayValue : value;
    }

}