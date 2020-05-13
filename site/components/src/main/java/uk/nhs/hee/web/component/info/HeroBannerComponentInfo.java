package uk.nhs.hee.web.component.info;

import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;

public interface HeroBannerComponentInfo {

    @Parameter(
            name = "heroBannerDocument",
            required = true,
            displayName = "Hero Banner Document"
    )
    @JcrPath(
            isRelative = true,
            pickerConfiguration = "cms-pickers/documents-only",
            pickerSelectableNodeTypes = {"heeweb:heroBanner"},
            pickerInitialPath = "HeroBanner"
    )
    String getHeroBannerDocument();

}

