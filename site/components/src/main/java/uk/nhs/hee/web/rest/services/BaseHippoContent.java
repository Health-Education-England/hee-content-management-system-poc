package uk.nhs.hee.web.rest.services;

public class BaseHippoContent {
    private String title;
    private String content;
    private String introduction;
    private String objectID;

    public BaseHippoContent() {
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }
}
