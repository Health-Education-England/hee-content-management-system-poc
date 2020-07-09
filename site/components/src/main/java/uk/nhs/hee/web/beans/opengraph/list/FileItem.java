package uk.nhs.hee.web.beans.opengraph.list;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class FileItem {
    private final String title;
    private final String url;
    private final String fileType;
    private final String modifiedBy;
    private final Date modifiedDate;


    public FileItem(
            final String title,
            final String url,
            final String fileType,
            final String modifiedBy,
            final Date modifiedDate) {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("title", title)
                .append("url", url)
                .append("fileType", fileType)
                .append("modifiedBy", modifiedBy)
                .append("modifiedDate", modifiedDate)
                .toString();
    }

}
