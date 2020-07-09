package uk.nhs.hee.web.component;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.services.hst.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.web.component.helper.ChannelHelper;
import uk.nhs.hee.web.component.helper.DocumentHelper;
import uk.nhs.hee.web.component.info.EducationHubListingComponentInfo;

import java.util.List;
import java.util.stream.Collectors;

@ParametersInfo(type = EducationHubListingComponentInfo.class)
public class EducationHubListingComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(EducationHubListingComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        List<Channel> educationHubChannels = ChannelHelper.getEducationHubChannels(request.getRequestContext());

        List<HippoBean> educationHubHomeBeans = educationHubChannels.stream()
                .map(channel -> DocumentHelper.getDocumentBean(request, channel.getContentRoot(), "hub/home"))
                .collect(Collectors.toList());

        EducationHubListingComponentInfo paramInfo = getComponentParametersInfo(request);
        request.setModel("educationHubTitle", paramInfo.getTitle());

        request.setModel("educationHubDocs", educationHubHomeBeans);
    }

}
