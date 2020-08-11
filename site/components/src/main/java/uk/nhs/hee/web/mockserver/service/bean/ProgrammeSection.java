package uk.nhs.hee.web.mockserver.service.bean;

public class ProgrammeSection {
    private String mainTitle;

    private String mainLinkType;

    private String mainLinkUrl;

    private String mainLinkLabel;

    private String cardType;

    private String cardTitle;

    private String cardSummary;

    private String cardImageUrl;

    private String cardLinkLabel;

    private String cardLinkUrl;

    private boolean mainLinkDisabled;

    private boolean renderProgrammeDetails;

    private boolean renderKeyStaffSectionBelowThisSection;

    private String mainBody;

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getMainLinkType() {
        return mainLinkType;
    }

    public void setMainLinkType(String mainLinkType) {
        this.mainLinkType = mainLinkType;
    }

    public String getMainLinkUrl() {
        return mainLinkUrl;
    }

    public void setMainLinkUrl(String mainLinkUrl) {
        this.mainLinkUrl = mainLinkUrl;
    }

    public String getMainLinkLabel() {
        return mainLinkLabel;
    }

    public void setMainLinkLabel(String mainLinkLabel) {
        this.mainLinkLabel = mainLinkLabel;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getCardSummary() {
        return cardSummary;
    }

    public void setCardSummary(String cardSummary) {
        this.cardSummary = cardSummary;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public String getCardLinkLabel() {
        return cardLinkLabel;
    }

    public void setCardLinkLabel(String cardLinkLabel) {
        this.cardLinkLabel = cardLinkLabel;
    }

    public String getCardLinkUrl() {
        return cardLinkUrl;
    }

    public void setCardLinkUrl(String cardLinkUrl) {
        this.cardLinkUrl = cardLinkUrl;
    }

    public boolean getMainLinkDisabled() {
        return mainLinkDisabled;
    }

    public void setMainLinkDisabled(boolean mainLinkDisabled) {
        this.mainLinkDisabled = mainLinkDisabled;
    }

    public boolean getRenderProgrammeDetails() {
        return renderProgrammeDetails;
    }

    public void setRenderProgrammeDetails(boolean renderProgrammeDetails) {
        this.renderProgrammeDetails = renderProgrammeDetails;
    }

    public boolean getRenderKeyStaffSectionBelowThisSection() {
        return renderKeyStaffSectionBelowThisSection;
    }

    public void setRenderKeyStaffSectionBelowThisSection(boolean renderKeyStaffSectionBelowThisSection) {
        this.renderKeyStaffSectionBelowThisSection = renderKeyStaffSectionBelowThisSection;
    }

    public String getMainBody() {
        return mainBody;
    }

    public void setMainBody(String mainBody) {
        this.mainBody = mainBody;
    }
}
