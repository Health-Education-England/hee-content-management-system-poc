package uk.nhs.hee.web.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoBean;

@HippoEssentialsGenerated(internalName = "heeweb:programmeSection")
@Node(jcrType = "heeweb:programmeSection")
public class ProgrammeSection extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "heeweb:mainTitle")
    public String getMainTitle() {
        return getSingleProperty("heeweb:mainTitle");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:mainLinkUrl")
    public String getMainLinkUrl() {
        return getSingleProperty("heeweb:mainLinkUrl");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:mainLinkLabel")
    public String getMainLinkLabel() {
        return getSingleProperty("heeweb:mainLinkLabel");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:cardType")
    public String getCardType() {
        return getSingleProperty("heeweb:cardType");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:cardTitle")
    public String getCardTitle() {
        return getSingleProperty("heeweb:cardTitle");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:cardSummary")
    public String getCardSummary() {
        return getSingleProperty("heeweb:cardSummary");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:cardLinkLabel")
    public String getCardLinkLabel() {
        return getSingleProperty("heeweb:cardLinkLabel");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:cardLinkUrl")
    public String getCardLinkUrl() {
        return getSingleProperty("heeweb:cardLinkUrl");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:mainLinkType")
    public String getMainLinkType() {
        return getSingleProperty("heeweb:mainLinkType");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:mainLinkDisabled")
    public Boolean getMainLinkDisabled() {
        return getSingleProperty("heeweb:mainLinkDisabled");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:renderProgrammeDetails")
    public Boolean getRenderProgrammeDetails() {
        return getSingleProperty("heeweb:renderProgrammeDetails");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:renderKeyStaffSectionBelowThisSection")
    public Boolean getRenderKeyStaffSectionBelowThisSection() {
        return getSingleProperty("heeweb:renderKeyStaffSectionBelowThisSection");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:mainBody")
    public HippoHtml getMainBody() {
        return getHippoHtml("heeweb:mainBody");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:cardImage")
    public HippoGalleryImageSet getCardImage() {
        return getLinkedBean("heeweb:cardImage", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "heeweb:cardDocument")
    public HippoBean getCardDocument() {
        return getLinkedBean("heeweb:cardDocument", HippoBean.class);
    }
}
