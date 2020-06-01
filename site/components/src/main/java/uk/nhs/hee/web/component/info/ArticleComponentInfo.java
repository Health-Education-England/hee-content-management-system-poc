package uk.nhs.hee.web.component.info;

import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;

public interface ArticleComponentInfo {

    @Parameter(
            name = "articleDocument",
            required = true,
            displayName = "Article Document"
    )
    @JcrPath(
            isRelative = false,
            pickerConfiguration = "cms-pickers/documents-only",
            pickerSelectableNodeTypes = {"heeweb:article"},
            pickerRootPath = "/content/documents"
    )
    String getArticleDocument();

}

