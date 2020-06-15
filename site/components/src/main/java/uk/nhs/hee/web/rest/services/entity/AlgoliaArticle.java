package uk.nhs.hee.web.rest.services.entity;

public class AlgoliaArticle {
    private String title;
    private String summary;
    private String[] category;
    private String region;
    private String speciality;
    private String subSpeciality;
    private Long lastUpdateAt;
    private String objectID;

    public AlgoliaArticle() {
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(Long lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }
}
