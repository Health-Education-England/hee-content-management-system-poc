package uk.nhs.hee.web.component.helper;

import org.hippoecm.hst.configuration.hosting.VirtualHost;
import org.hippoecm.hst.configuration.hosting.VirtualHosts;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.services.hst.Channel;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChannelHelper {

    public static List<Channel> getRegionalChannels(HstRequestContext requestContext) {
        return getChannelByHEEChannelType(requestContext, "regional");
    }

    public static List<Channel> getEducationHubChannels(HstRequestContext requestContext) {
        return getChannelByHEEChannelType(requestContext, "education-hub");
    }

    public static Channel getChannelById(HstRequestContext requestContext, String channelId) {
        Map<String, Channel> channels = getChannels(requestContext);

        List<Channel> channelListWithGivenId = channels.values().stream()
                .filter(channel -> !channel.isPreview() && channelId.equals(channel.getId()))
                .collect(Collectors.toList());

        if (channelListWithGivenId.size() != 1) {
            return null;
        }

        return channelListWithGivenId.get(0);
    }

    private static List<Channel> getChannelByHEEChannelType(HstRequestContext requestContext, String heeChannelType) {
        Map<String, Channel> channels = getChannels(requestContext);

        return channels.values().stream()
                .filter(
                        channel -> !channel.isPreview() &&
                                heeChannelType.equals(channel.getProperties().get("channelType")))
                .sorted(Comparator.comparing(Channel::getName))
                .collect(Collectors.toList());
    }

    private static Map<String, Channel> getChannels(HstRequestContext requestContext) {
        VirtualHost virtualHost = requestContext.getVirtualHost();
        VirtualHosts virtualHosts = virtualHost.getVirtualHosts();

        return virtualHosts.getChannels(virtualHost.getHostGroupName());
    }
}
