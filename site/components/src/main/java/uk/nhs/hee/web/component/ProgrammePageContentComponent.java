package uk.nhs.hee.web.component;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.services.hst.Channel;
import uk.nhs.hee.web.beans.HubDocument;
import uk.nhs.hee.web.beans.ProgrammeDocument;
import uk.nhs.hee.web.component.helper.ChannelHelper;
import uk.nhs.hee.web.component.helper.TrainingHubHelper;

public class ProgrammePageContentComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        ProgrammeDocument programmeDocument = request.getRequestContext().getContentBean(ProgrammeDocument.class);

        request.setModel("document", programmeDocument);

        addParentDocumentTitleToModel(
                request,
                TrainingHubHelper.getSubSpecialtyDocument(programmeDocument),
                "subSpecialty");
        addParentDocumentTitleToModel(
                request,
                TrainingHubHelper.getTrainingTypeDocument(programmeDocument),
                "trainingType");
        addRegionToModel(request, programmeDocument);
    }

    private void addParentDocumentTitleToModel(HstRequest request, HubDocument parentHubDocument, String modelKey) {
        request.setModel(
                modelKey,
                StringUtils.isEmpty(parentHubDocument.getTitle()) ? StringUtils.EMPTY : parentHubDocument.getTitle());
    }

    private void addRegionToModel(HstRequest request, ProgrammeDocument programmeDocument) {
        Channel regionalChannel =
                ChannelHelper.getChannelById(request.getRequestContext(), programmeDocument.getRegion());

        String regionName = StringUtils.EMPTY;
        if (regionalChannel != null) {
            regionName = regionalChannel.getName();
        }

        request.setModel("regionName", regionName);
    }
}
