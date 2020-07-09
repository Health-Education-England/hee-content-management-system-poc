package uk.nhs.hee.web.component;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.hee.web.beans.HubDocument;
import uk.nhs.hee.web.beans.Link;
import uk.nhs.hee.web.component.helper.DocumentHelper;
import uk.nhs.hee.web.component.helper.TrainingHubHelper;

import java.util.List;
import java.util.stream.Collectors;

public class BreadcrumbComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        List<HubDocument> parentHubDocuments = TrainingHubHelper.getParentHubDocuments(request);

        List<Link> breadCrumbLinks = parentHubDocuments.stream()
                .map(hubDocument -> new Link(
                        hubDocument.getPath().endsWith("/hub/home/home") ? "Home" : hubDocument.getSubHubType(),
                        DocumentHelper.getSiteContextUrl(request, hubDocument)))
                .collect(Collectors.toList());

        request.setModel("breadCrumbLinks", breadCrumbLinks);
    }

}
