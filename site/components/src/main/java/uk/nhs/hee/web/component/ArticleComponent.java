package uk.nhs.hee.web.component;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;

import uk.nhs.hee.web.beans.Article;
import uk.nhs.hee.web.component.helper.ComponentHelper;
import uk.nhs.hee.web.component.info.ArticleComponentInfo;

/**
 * Component class for <code>article</code> catalog.
 */
@ParametersInfo(type = ArticleComponentInfo.class)
public class ArticleComponent extends CommonComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        request.setModel(
                "articleDocument",
                ComponentHelper.getHippoBeanForPath(
                        this,
                        "articleDocument",
                        getClass().getAnnotation(ParametersInfo.class),
                        ((ArticleComponentInfo) getComponentParametersInfo(request)).getArticleDocument(),
                        Article.class));
    }

}
