package uk.nhs.hee.web.beans.opengraph.list;

import java.util.List;
import java.util.Map;

public class User {
    private String username;
    private String displayName;
    private String jobTitle;
    private List<String> groups;
    private Map<String, List<FileItem>> siteFiles;

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
    /**
     * @return the siteFiles
     */
    public Map<String, List<FileItem>> getSiteFiles() {
        return siteFiles;
    }
    /**
     * @param siteFiles the siteFiles to set
     */
    public void setSiteFiles(Map<String, List<FileItem>> siteFiles) {
        this.siteFiles = siteFiles;
    }

}
