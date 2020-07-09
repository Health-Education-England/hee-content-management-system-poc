package uk.nhs.hee.web.configuration.channel;


import org.hippoecm.hst.configuration.channel.ChannelInfo;
import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;

public interface WebsiteChannelInfo extends ChannelInfo {
    @Parameter(name = "channelType", displayName = "Channel Type", required = true)
    @DropDownList({"global", "regional", "education-hub"})
    String getChannelType();

    @Parameter(name = "whiteHeaderBg",
            displayName = "Apply White Header Background ?",
            defaultValue = "false")
    Boolean getWhiteHeaderBg();

    @Parameter(name = "orgName",
            displayName = "Org. Name",
            defaultValue = "Health Education England")
    String getOrgName();

    @Parameter(name = "orgSplit",
            displayName = "Org. Split",
            required = true)
    String getOrgSplit();

    @Parameter(name = "orgDescriptor",
            displayName = "Org. Descriptor")
    String getOrgDescriptor();

}
