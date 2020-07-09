package uk.nhs.hee.web.component;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsMenuComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsMenuComponentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.web.beans.HeaderTheme;
import uk.nhs.hee.web.configuration.channel.WebsiteChannelInfo;

@ParametersInfo(type = EssentialsMenuComponentInfo.class)
public class HEEMenuComponent extends EssentialsMenuComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(HEEMenuComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        Mount mount = request.getRequestContext().getResolvedMount().getMount();

        if (!mount.isExplicit()) {
            mount = mount.getParent();
        }

        WebsiteChannelInfo channelInfo = mount.getChannelInfo();

        HeaderTheme headerTheme =
                new HeaderTheme(
                        channelInfo.getChannelType(),
                        channelInfo.getWhiteHeaderBg(),
                        channelInfo.getOrgName(),
                        channelInfo.getOrgSplit(),
                        channelInfo.getOrgDescriptor());

        LOGGER.debug("Header Theme => {}", headerTheme);

        request.setModel("headerTheme", headerTheme);
    }

}
