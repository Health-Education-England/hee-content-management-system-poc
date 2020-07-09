package uk.nhs.hee.web.platform.services.channel;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.Session;
import javax.jcr.security.Privilege;

import org.hippoecm.hst.container.RequestContextProvider;
import org.onehippo.cms7.services.hst.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Restricts/hides the channel if the user doesn't have one of
 * <code>hippo:author, hippo:editor and hippo:admin</code> privileges.
 * This essentially means that the user has got readonly access
 * to the channel content folder (probably to use the corresponding
 * channel content) and the channel should be hidden from the user.
 * <p>
 * For instance, if a user has readonly (<code>jcr:read</code>) privilege to
 * global channel content, then global channel should be hidden from the user.
 */
public class GlobalChannelAccessFilter implements BiPredicate<Session, Channel> {
    public static final List<String> AUTHOR_AND_EDITOR_PRIVILEGES =
            Arrays.asList("hippo:author", "hippo:editor", "hippo:admin");

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalChannelAccessFilter.class);

    @Override
    public boolean test(Session cmsSession, Channel channel) {
        try {
            if (RequestContextProvider.get() == null) {
                // invoked by background thread, no filtering
                return true;
            }

            LOGGER.info("Channel name => '{}' and it's content root => '{}'", channel.getName(), channel.getContentRoot());

            Privilege[] privileges = cmsSession.getAccessControlManager().getPrivileges(channel.getContentRoot());

            LOGGER.info("User has got {} privileges on the content folder '{}'",
                    Stream.of(privileges).map(Privilege::getName).collect(Collectors.toList()),
                    channel.getContentRoot());

            return Stream.of(privileges)
                    .map(Privilege::getName)
                    .anyMatch(AUTHOR_AND_EDITOR_PRIVILEGES::contains);
        } catch (Exception e) {
            LOGGER.warn(
                    "Caught error '{}' while checking the current user privilege for the channel '{}'",
                    e.getMessage(),
                    channel.getName(),
                    e);
            return false;
        }
    }

}
