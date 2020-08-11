package uk.nhs.hee.web.component;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.services.hst.Channel;
import uk.nhs.hee.web.beans.Link;
import uk.nhs.hee.web.component.helper.ChannelHelper;
import uk.nhs.hee.web.component.helper.ResourceBundleHelper;

import java.util.List;
import java.util.stream.Collectors;

public class TrainingByLocalTeamsComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        ResourceBundleHelper.addValueToModel(
                request,
                "medical-education-hub.hub",
                "training-by-local-teams.title",
                "trainingByLocalTeamsTitle");

        addRegionalChannelLinksToModel(request);
    }

    private void addRegionalChannelLinksToModel(HstRequest request) {
        List<Channel> regionalChannels = ChannelHelper.getRegionalChannels(request.getRequestContext());

        if (regionalChannels.isEmpty()) {
            return;
        }

        List<Link> regionalChannelLinks =
                regionalChannels.stream()
                        .map(channel -> new Link(channel.getName(), channel.getUrl()))
                        .collect(Collectors.toList());

        request.setModel("trainingByLocalTeamsLinks", regionalChannelLinks);
    }
}
