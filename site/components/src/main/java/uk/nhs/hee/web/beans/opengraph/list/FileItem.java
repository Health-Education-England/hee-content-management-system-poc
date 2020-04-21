package uk.nhs.hee.web.beans.opengraph.list;

import java.util.Date;

public class FileItem {
    private String title;
    private String url;
    private String fileType;
    private String modifiedBy;
    private Date modifiedDate;


    public FileItem(String title, String url, String fileType, String modifiedBy, Date modifiedDate) {
        super();
        this.title = title;
        this.url = url;
        this.fileType = fileType;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @return the modifiedBy
     */
    public String getModifiedBy() {
        return modifiedBy;
    }

    /**
     * @return the modifiedDate
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

}
