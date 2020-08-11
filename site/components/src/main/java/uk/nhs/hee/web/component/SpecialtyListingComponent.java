package uk.nhs.hee.web.component;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.hee.web.component.helper.SubHubListingComponentHelper;

public class SpecialtyListingComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        SubHubListingComponentHelper.addSubHubListingToModel(request, "specialty.subhub.listing.title");
    }

}
