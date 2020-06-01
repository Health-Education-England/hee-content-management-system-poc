package uk.nhs.hee.web.component;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.hee.web.beans.HeroBanner;
import uk.nhs.hee.web.component.info.HeroBannerComponentInfo;

@ParametersInfo(type = HeroBannerComponentInfo.class)
public class HeroBannerComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        HeroBannerComponentInfo paramInfo = getComponentParametersInfo(request);
        request.setModel(
                "heroBannerDocument",
                getHippoBeanForPath(paramInfo.getHeroBannerDocument(), HeroBanner.class));
    }
}
