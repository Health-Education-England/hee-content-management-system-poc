package uk.nhs.hee.web.component;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.hee.web.component.info.SearchComponentInfo;

@ParametersInfo(type = SearchComponentInfo.class)
public class SearchComponent extends CommonComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        SearchComponentInfo paramInfo = getComponentParametersInfo(request);
        request.setAttribute(REQUEST_ATTR_PARAM_INFO, paramInfo);
    }

}
