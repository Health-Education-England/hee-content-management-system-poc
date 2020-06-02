package uk.nhs.hee.web.configuration.channel.listener;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.hippoecm.hst.configuration.channel.Blueprint;
import org.hippoecm.hst.configuration.channel.ChannelManagerEvent;
import org.hippoecm.hst.configuration.channel.ChannelManagerEventListener;
import org.hippoecm.hst.configuration.channel.ChannelManagerEventListenerException;
import org.onehippo.cms7.services.hst.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostChannelCreationStepsListener implements ChannelManagerEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostChannelCreationStepsListener.class);


    @Override
    public void channelCreated(ChannelManagerEvent event)
            throws ChannelManagerEventListenerException {
        // LOGGER.info("A channel has been created. {}", channelManagerEventToString(event));
    }

    @Override
    public void channelUpdated(ChannelManagerEvent event) {

    }

    private String channelManagerEventToString(ChannelManagerEvent event) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("{ ");

        Blueprint blueprint = event.getBlueprint();
        // blueprint can be null
        if (blueprint != null) {
            sb.append("blueprint: [ ");
            sb.append(blueprint.getId()).append(", ");
            sb.append(blueprint.getName()).append(", ");
            sb.append(blueprint.getDescription());
            sb.append(" ], ");
        }

        Channel channel = event.getChannel();
        // channel will never be null
        sb.append("channel: [ ");
        sb.append(event.getChannel().getId()).append(", ");
        sb.append(channel.getName()).append(", ");
        sb.append(channel.getContentRoot());
        sb.append(" ], ");

        Node configRootNode = event.getConfigRootNode();

        try {
            if (configRootNode != null) {
                sb.append("configRootNode: ");
                sb.append(configRootNode.getPath());
            }
        } catch (RepositoryException e) {
            LOGGER.error("Failed to read channel node path", e);
        }

        sb.append(" }");
        return sb.toString();
    }
}