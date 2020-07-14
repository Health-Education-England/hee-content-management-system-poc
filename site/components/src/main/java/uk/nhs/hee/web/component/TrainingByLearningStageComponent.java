package uk.nhs.hee.web.component;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.hee.web.component.helper.ResourceBundleHelper;
import uk.nhs.hee.web.component.helper.TrainingHubHelper;

import java.util.List;

public class TrainingByLearningStageComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        List<HippoBean> subHubHippoBeans = TrainingHubHelper.getSubHubs(request);

        if (subHubHippoBeans.isEmpty()) {
            return;
        }

        ResourceBundleHelper.addValueToModel(
                request,
                "medical-education-hub.hub",
                "training-by-learning-stage.title",
                "title");

        request.setModel("documents", subHubHippoBeans);
    }
}
