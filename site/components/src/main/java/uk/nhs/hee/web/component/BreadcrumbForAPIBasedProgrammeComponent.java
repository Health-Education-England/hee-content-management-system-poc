package uk.nhs.hee.web.component;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.web.beans.HubDocument;
import uk.nhs.hee.web.beans.Link;
import uk.nhs.hee.web.component.helper.DocumentHelper;
import uk.nhs.hee.web.component.helper.TrainingHubHelper;
import uk.nhs.hee.web.mockserver.service.MockServerServiceFactory;
import uk.nhs.hee.web.mockserver.service.bean.Programme;

import java.util.List;
import java.util.stream.Collectors;

public class BreadcrumbForAPIBasedProgrammeComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(BreadcrumbForAPIBasedProgrammeComponent.class);

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

            HippoBean siteContentBean = request.getRequestContext().getSiteContentBaseBean();
            String subSpecialtyId = programme.getSubSpecialtyId();
            HubDocument subSpecialtyHubDocument = TrainingHubHelper.getHubDocument(siteContentBean, subSpecialtyId);

            if (subSpecialtyHubDocument == null) {
                return;
            }

            List<HubDocument> parentHubDocuments = TrainingHubHelper.getParentHubDocuments(subSpecialtyHubDocument);
            parentHubDocuments.add(subSpecialtyHubDocument);

            List<Link> breadCrumbLinks = parentHubDocuments.stream()
                    .map(hubDocument -> new Link(
                            hubDocument.getPath().endsWith("/hub/home/home") ? "Home" : hubDocument.getSubHubType(),
                            DocumentHelper.getSiteContextUrl(request, hubDocument)))
                    .collect(Collectors.toList());

            request.setModel("breadCrumbLinks", breadCrumbLinks);
        } catch (ResourceException e) {
            LOGGER.error(
                    "Caught error '{}' while getting programme for the path '{}' via (MockServer) API",
                    e.getMessage(),
                    programmePath,
                    e);
        }
    }

}
