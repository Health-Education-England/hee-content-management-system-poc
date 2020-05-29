package uk.nhs.hee.web.beans;

public class LogoOrgTheme {

    private String orgName;
    private String orgSplit;
    private String orgDescriptor;


    public LogoOrgTheme(String orgName, String orgSplit, String orgDescriptor) {
        super();
        this.orgName = orgName;
        this.orgSplit = orgSplit;
        this.orgDescriptor = orgDescriptor;
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

}
