package uk.nhs.hee.web.mockserver.service.bean;

import java.util.List;

public class Programme {
    private String self;

    private String specialty;

    private String subSpecialtyName;

    private String subSpecialtyId;

    private String trainingType;

    private String regionName;

    private String regionId;

    private String regionPageUrl;

    private String name;

    private String displayName;

    private int totalPosts;

    private int numberOfVacancies;

    private String competitionRatio;

    private String gmcNTSScore;

    private String entryRequirementsLinkUrl;

    private String contractType;

    private String partTimeContractType;

    private String educationType;

    private String qualification;

    private String durationInYears;

    private long reopeningCalendar;

    private List<KeyStaff> keyStaffs;

    private List<ProgrammeSection> programmeSections;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getSubSpecialtyName() {
        return subSpecialtyName;
    }

    public void setSubSpecialtyName(String subSpecialtyName) {
        this.subSpecialtyName = subSpecialtyName;
    }

    public String getSubSpecialtyId() {
        return subSpecialtyId;
    }

    public void setSubSpecialtyId(String subSpecialtyId) {
        this.subSpecialtyId = subSpecialtyId;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionPageUrl() {
        return regionPageUrl;
    }

    public void setRegionPageUrl(String regionPageUrl) {
        this.regionPageUrl = regionPageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(int totalPosts) {
        this.totalPosts = totalPosts;
    }

    public int getNumberOfVacancies() {
        return numberOfVacancies;
    }

    public void setNumberOfVacancies(int numberOfVacancies) {
        this.numberOfVacancies = numberOfVacancies;
    }

    public String getCompetitionRatio() {
        return competitionRatio;
    }

    public void setCompetitionRatio(String competitionRatio) {
        this.competitionRatio = competitionRatio;
    }

    public String getGmcNTSScore() {
        return gmcNTSScore;
    }

    public void setGmcNTSScore(String gmcNTSScore) {
        this.gmcNTSScore = gmcNTSScore;
    }

    public String getEntryRequirementsLinkUrl() {
        return entryRequirementsLinkUrl;
    }

    public void setEntryRequirementsLinkUrl(String entryRequirementsLinkUrl) {
        this.entryRequirementsLinkUrl = entryRequirementsLinkUrl;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getPartTimeContractType() {
        return partTimeContractType;
    }

    public void setPartTimeContractType(String partTimeContractType) {
        this.partTimeContractType = partTimeContractType;
    }

    public String getEducationType() {
        return educationType;
    }

    public void setEducationType(String educationType) {
        this.educationType = educationType;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDurationInYears() {
        return durationInYears;
    }

    public void setDurationInYears(String durationInYears) {
        this.durationInYears = durationInYears;
    }

    public long getReopeningCalendar() {
        return reopeningCalendar;
    }

    public void setReopeningCalendar(long reopeningCalendar) {
        this.reopeningCalendar = reopeningCalendar;
    }

    public List<KeyStaff> getKeyStaffs() {
        return keyStaffs;
    }

    public void setKeyStaffs(List<KeyStaff> keyStaffs) {
        this.keyStaffs = keyStaffs;
    }

    public List<ProgrammeSection> getProgrammeSections() {
        return programmeSections;
    }

    public void setProgrammeSections(List<ProgrammeSection> programmeSections) {
        this.programmeSections = programmeSections;
    }
}
