package uk.nhs.hee.web.component.info;

import org.hippoecm.hst.core.parameters.Parameter;

public interface EducationHubListingComponentInfo {

    @Parameter(
            name = "title",
            required = false,
            displayName = "Title"
    )
    String getTitle();

}

