package uk.nhs.hee.web.component;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsMenuComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsMenuComponentInfo;

import uk.nhs.hee.web.beans.LogoOrgTheme;
import uk.nhs.hee.web.configuration.channel.WebsiteChannelInfo;

@ParametersInfo(type = EssentialsMenuComponentInfo.class)
public class HEEMenuComponent extends EssentialsMenuComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        // setComponentId(request, response);

        Mount mount = request.getRequestContext().getResolvedMount().getMount();

        if (!mount.isExplicit()) {
            mount = mount.getParent();
        }

        WebsiteChannelInfo channelInfo = mount.getChannelInfo();

        final LogoOrgTheme logoOrgTheme =
                new LogoOrgTheme(channelInfo.getOrgName(), channelInfo.getOrgSplit(), channelInfo.getOrgDescriptor());

        // request.setModel("channelInfo", channelInfo);
        request.setModel("logoOrgTheme", logoOrgTheme);
    }

}
