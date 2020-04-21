package uk.nhs.hee.web.component.info;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;

import uk.nhs.hee.web.component.parameters.SharepointSiteValueListProvider;

public interface HEEWebSharepointFilesComponentInfo {

    @Parameter(name = "sharepointSite", displayName = "Sharepoint Site", required = true)
    // @DropDownList(value = {"HEE Communication Site", "Test Site"})
    @DropDownList(valueListProvider = SharepointSiteValueListProvider.class)
    String getSharepointSite();

}
