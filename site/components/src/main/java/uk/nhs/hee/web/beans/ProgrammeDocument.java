package uk.nhs.hee.web.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import java.util.Calendar;
import java.util.List;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import uk.nhs.hee.web.beans.ProgrammeSection;

@HippoEssentialsGenerated(internalName = "heeweb:programmeDocument")
@Node(jcrType = "heeweb:programmeDocument")
public class ProgrammeDocument extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "heeweb:numberOfVacancies")
    public Long getNumberOfVacancies() {
        return getSingleProperty("heeweb:numberOfVacancies");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:totalPosts")
    public Long getTotalPosts() {
        return getSingleProperty("heeweb:totalPosts");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:competitionRatio")
    public String getCompetitionRatio() {
        return getSingleProperty("heeweb:competitionRatio");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:gmcNTSScore")
    public String getGmcNTSScore() {
        return getSingleProperty("heeweb:gmcNTSScore");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:entryRequirementsLinkUrl")
    public String getEntryRequirementsLinkUrl() {
        return getSingleProperty("heeweb:entryRequirementsLinkUrl");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:contractType")
    public String getContractType() {
        return getSingleProperty("heeweb:contractType");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:partTimeContractType")
    public String getPartTimeContractType() {
        return getSingleProperty("heeweb:partTimeContractType");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:educationType")
    public String getEducationType() {
        return getSingleProperty("heeweb:educationType");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:qualification")
    public String getQualification() {
        return getSingleProperty("heeweb:qualification");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:keyStaffs")
    public List<HippoBean> getKeyStaffs() {
        return getLinkedBeans("heeweb:keyStaffs", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "heeweb:programmeSections")
    public List<ProgrammeSection> getProgrammeSections() {
        return getChildBeansByName("heeweb:programmeSections",
                ProgrammeSection.class);
    }

    @HippoEssentialsGenerated(internalName = "heeweb:region")
    public String getRegion() {
        return getSingleProperty("heeweb:region");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:durationInYears")
    public String getDurationInYears() {
        return getSingleProperty("heeweb:durationInYears");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:reopeningCalendar")
    public Calendar getReopeningCalendar() {
        return getSingleProperty("heeweb:reopeningCalendar");
    }
}
