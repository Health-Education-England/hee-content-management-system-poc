package uk.nhs.hee.web.component;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.web.beans.HubDocument;
import uk.nhs.hee.web.mockserver.service.MockServerServiceFactory;
import uk.nhs.hee.web.mockserver.service.bean.Programme;

import java.util.List;

public class SpecialtyProgrammeListingByAPIComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialtyProgrammeListingByAPIComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        HubDocument hubDocument =
                request.getRequestContext().getContentBean(HubDocument.class);

        request.setModel("programmeListingTitle", hubDocument.getTitle() + " programmes [source: API]");

        request.setModel("currentPageType", "specialty");

        addSpecialtyProgrammesToModel(request, hubDocument.getSubHubType());
    }

    private void addSpecialtyProgrammesToModel(HstRequest request, String subHubType) {
        String specialty = subHubType.toLowerCase();

        try {
            List<Programme> programmes = new MockServerServiceFactory().getProgrammeService()
                    .getProgrammesBySpecialty(specialty);

            if (programmes == null || programmes.isEmpty()) {
                return;
            }

            request.setModel("programmes", programmes);
        } catch (ResourceException e) {
            LOGGER.error(
                    "Caught error '{}' while getting programmes for the specialty '{}' via (MockServer) API",
                    e.getMessage(),
                    specialty,
                    e);
        }
    }

}
