package uk.nhs.hee.web.configuration.channel;


import org.hippoecm.hst.configuration.channel.ChannelInfo;
import org.hippoecm.hst.core.parameters.Parameter;

public interface WebsiteChannelInfo extends ChannelInfo {

    @Parameter(name = "orgName",
            displayName = "Org. Name",
            defaultValue = "Health Education England")
    String getOrgName();

    @Parameter(name = "orgSplit",
            displayName = "Org. Split",
            required = true)
    String getOrgSplit();

    @Parameter(name = "orgDescriptor",
            displayName = "Org. Descriptor",
            required = false)
    String getOrgDescriptor();

}
