package uk.nhs.hee.web.beans;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class HeaderTheme {

    private final boolean whiteHeaderBg;
    private final String orgName;
    private final String orgSplit;
    private final String orgDescriptor;
    private final String channelType;


    public HeaderTheme(
            String channelType,
            boolean whiteHeaderBg,
            String orgName,
            String orgSplit,
            String orgDescriptor) {
        super();
        this.channelType = channelType;
        this.whiteHeaderBg = whiteHeaderBg;
        this.orgName = orgName;
        this.orgSplit = orgSplit;
        this.orgDescriptor = orgDescriptor;
    }

    public String getChannelType() {
        return channelType;
    }

    public boolean getWhiteHeaderBg() {
        return whiteHeaderBg;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgSplit() {
        return orgSplit;
    }

    public String getOrgDescriptor() {
        return orgDescriptor;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("channelType", channelType)
                .append("whiteHeaderBg", whiteHeaderBg)
                .append("orgName", orgName)
                .append("orgSplit", orgSplit)
                .append("orgDescriptor", orgDescriptor)
                .toString();
    }

}
