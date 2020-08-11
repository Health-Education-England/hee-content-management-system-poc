package uk.nhs.hee.web.beans;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.cms7.services.hst.Channel;
import uk.nhs.hee.web.component.helper.ChannelHelper;
import uk.nhs.hee.web.component.helper.DocumentHelper;
import uk.nhs.hee.web.component.helper.TrainingHubHelper;

import java.io.Serializable;
import java.util.Calendar;

public class ProgrammeForListing implements Serializable {
    private static final long serialVersionUID = -6527098881488291551L;
    private final String programmePageUrl;
    private final String durationInYears;
    private final String contractType;
    private final Long numberOfVacancies;
    private final Calendar reopeningCalendar;
    private String subSpecialty;
    private String regionName;
    private String regionPageUrl;


    public ProgrammeForListing(HstRequest request, ProgrammeDocument programmeDocument) {
        setSubSpecialty(programmeDocument);
        programmePageUrl = DocumentHelper.getSiteContextUrl(request, programmeDocument);
        setRegionNameAndUrl(request, programmeDocument);
        durationInYears = programmeDocument.getDurationInYears();
        contractType = programmeDocument.getContractType();
        numberOfVacancies = programmeDocument.getNumberOfVacancies();
        reopeningCalendar = programmeDocument.getReopeningCalendar();
    }

    private void setRegionNameAndUrl(HstRequest request, ProgrammeDocument programmeDocument) {
        Channel regionalChannel =
                ChannelHelper.getChannelById(request.getRequestContext(), programmeDocument.getRegion());

        if (regionalChannel == null) {
            regionName = StringUtils.EMPTY;
            regionPageUrl = StringUtils.EMPTY;
        } else {
            regionName = regionalChannel.getName();
            regionPageUrl = regionalChannel.getUrl();
        }
    }

    public String getSubSpecialty() {
        return subSpecialty;
    }

    private void setSubSpecialty(ProgrammeDocument programmeDocument) {
        HubDocument subSpecialtyDocument = TrainingHubHelper.getSubSpecialtyDocument(programmeDocument);
        if (subSpecialtyDocument != null) {
            subSpecialty = subSpecialtyDocument.getTitle();
        } else {
            subSpecialty = StringUtils.EMPTY;
        }
    }

    public String getProgrammePageUrl() {
        return programmePageUrl;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getRegionPageUrl() {
        return regionPageUrl;
    }

    public String getDurationInYears() {
        return durationInYears;
    }

    public String getContractType() {
        return contractType;
    }

    public Long getNumberOfVacancies() {
        return numberOfVacancies;
    }

    public Calendar getReopeningCalendar() {
        return reopeningCalendar;
    }

}
