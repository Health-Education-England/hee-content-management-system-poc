package uk.nhs.hee.web.beans;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class HeaderTheme {

    private boolean whiteHeaderBg;
    private String orgName;
    private String orgSplit;
    private String orgDescriptor;


    public HeaderTheme(boolean whiteHeaderBg, String orgName, String orgSplit, String orgDescriptor) {
        super();
        this.whiteHeaderBg = whiteHeaderBg;
        this.orgName = orgName;
        this.orgSplit = orgSplit;
        this.orgDescriptor = orgDescriptor;
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
                .append("whiteHeaderBg", whiteHeaderBg)
                .append("orgName", orgName)
                .append("orgSplit", orgSplit)
                .append("orgDescriptor", orgDescriptor)
                .toString();
    }

}
