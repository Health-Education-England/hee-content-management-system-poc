package uk.nhs.hee.web.component;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.web.mockserver.service.MockServerServiceFactory;
import uk.nhs.hee.web.mockserver.service.bean.Programme;

public class ProgrammePageContentByAPIComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammePageContentByAPIComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        String requestPath = request.getPathInfo();
        String programmePath = requestPath.substring(requestPath.lastIndexOf("/"));

        try {
            Programme programme = new MockServerServiceFactory().getProgrammeService()
                    .getProgramme(programmePath);

            if (programme == null) {
                return;
            }

            request.setModel("programme", programme);
        } catch (ResourceException e) {
            LOGGER.error(
                    "Caught error '{}' while getting programme for the path '{}' via (MockServer) API",
                    e.getMessage(),
                    programmePath,
                    e);
        }
    }

}
