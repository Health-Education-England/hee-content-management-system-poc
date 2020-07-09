package uk.nhs.hee.web.beans.opengraph.list;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class User {
    private String username;
    private String displayName;
    private String jobTitle;
    private List<String> groups;

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }
    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    /**
     * @return the jobTitle
     */
    public String getJobTitle() {
        return jobTitle;
    }
    /**
     * @param jobTitle the jobTitle to set
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    /**
     * @return the groups
     */
    public List<String> getGroups() {
        /* if (groups == null) {
            Collections.emptyList();
        } */
        return groups;
    }
    /**
     * @param groups the groups to set
     */
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("username", username)
                .append("displayName", displayName)
                .append("jobTitle", jobTitle)
                .append("groups", groups)
                .toString();
    }

}
