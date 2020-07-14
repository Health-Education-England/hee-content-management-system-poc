package uk.nhs.hee.web.component;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.hee.web.component.helper.ResourceBundleHelper;
import uk.nhs.hee.web.component.helper.TrainingHubHelper;

import java.util.List;

public class SubHubListingComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        List<HippoBean> subHubHippoBeans = TrainingHubHelper.getSubHubs(request);

        if (subHubHippoBeans.isEmpty()) {
            return;
        }

        ResourceBundleHelper.addSubHubListingTitleToModel(request);
        request.setModel("documents", subHubHippoBeans);
    }

}
