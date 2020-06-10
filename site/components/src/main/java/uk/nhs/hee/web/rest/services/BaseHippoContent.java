package uk.nhs.hee.web.rest.services;

public class BaseHippoContent {
    private String title;
    private String content;
    private String introduction;
    private String[] category;
    private String region;
    private String speciality;
    private String subSpeciality;
    private String objectID;

    public BaseHippoContent() {
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

    public void setCategory(String[] category) {
        this.category = category;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setSubSpeciality(String subSpeciality) {
        this.subSpeciality = subSpeciality;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
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

    public String[] getCategory() {
        return category;
    }

    public String getRegion() {
        return region;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getSubSpeciality() {
        return subSpeciality;
    }

    public String getObjectID() {
        return objectID;
    }
}
