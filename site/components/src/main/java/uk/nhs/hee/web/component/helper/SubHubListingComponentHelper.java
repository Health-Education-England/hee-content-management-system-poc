package uk.nhs.hee.web.component.helper;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;

import java.util.List;

public class SubHubListingComponentHelper {

    public static void addSubHubListingToModel(HstRequest request, String resourceBundleKey) {
        List<HippoBean> subHubHippoBeans = TrainingHubHelper.getSubHubs(request);

        if (subHubHippoBeans.isEmpty()) {
            return;
        }

        ResourceBundleHelper.addSubHubListingTitleToModel(request, resourceBundleKey);
        request.setModel("documents", subHubHippoBeans);
    }

}
