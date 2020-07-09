package uk.nhs.hee.web.selection.frontend.provider;

import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.Plugin;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.session.UserSession;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.platform.api.PlatformServices;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.hst.Channel;
import org.onehippo.forge.selection.frontend.model.ListItem;
import org.onehippo.forge.selection.frontend.model.ValueList;
import org.onehippo.forge.selection.frontend.provider.IValueListProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RegionalChannelValueListProvider extends Plugin implements IValueListProvider {

    public static final String DEFAULT_REGIONAL_CHANNEL_SERVICE = "service.valuelist.channel.region";
    public static final String REGIONAL_CHANNEL_VALUE_LIST = "regionalChannelValueList";
    /**
     * Deprecated because the method that uses it is deprecated
     */
    @Deprecated
    private final static String CONFIG_SOURCE = "source";
    private static final Logger log = LoggerFactory.getLogger(RegionalChannelValueListProvider.class);
    private static final long serialVersionUID = 9078244426870714416L;

    public RegionalChannelValueListProvider(IPluginContext context, IPluginConfig config) {
        super(context, config);

        String name = config.getString(IValueListProvider.SERVICE, DEFAULT_REGIONAL_CHANNEL_SERVICE);
        context.registerService(this, name);

        if (log.isDebugEnabled()) {
            log.debug(getClass().getName() + " registered under " + name);
        }
    }

    private static List<Channel> getRegionalChannels(HstRequestContext requestContext) {
        PlatformServices platformServices = HippoServiceRegistry.getService(PlatformServices.class);

        List<Channel> channels = platformServices.getChannelService()
                .getLiveChannels(requestContext.getVirtualHost().getHostGroupName());

        if (channels.isEmpty()) {
            return Collections.emptyList();
        }

        return channels.stream()
                .filter(channel -> "regional".equals(channel.getProperties().get("channelType")))
                .sorted(Comparator.comparing(Channel::getName))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueList getValueList(IPluginConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Argument 'config' may not be null");
        }
        return getValueList(config.getString(CONFIG_SOURCE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueList getValueList(String name) {
        return getValueList(name, null/*locale*/);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ValueList getValueList(String name, Locale locale) {
        return getValueList(name, locale, obtainSession());
    }

    @Override
    public ValueList getValueList(String name, Locale locale, Session session) {
        if (REGIONAL_CHANNEL_VALUE_LIST.equals(name)) {
            return getRegionalChannels(RequestContextProvider.get()).stream()
                    .map(channel -> new ListItem(channel.getId(), channel.getName()))
                    .collect(Collectors.toCollection(ValueList::new));
        }

        return new ValueList();
    }

    @Override
    public List<String> getValueListNames() {
        return Collections.singletonList(REGIONAL_CHANNEL_VALUE_LIST);
    }

    /**
     * Gets the JCR {@link Session} from the Wicket
     * {@link org.apache.wicket.Session}
     *
     * @return {@link Session}
     */
    protected Session obtainSession() {
        UserSession userSession = (UserSession) org.apache.wicket.Session.get();
        return userSession.getJcrSession();
    }

}