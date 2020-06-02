package uk.nhs.hee.web.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import java.util.List;
import org.hippoecm.hst.content.beans.standard.HippoBean;

@HippoEssentialsGenerated(internalName = "heeweb:article")
@Node(jcrType = "heeweb:article")
public class Article extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "heeweb:title")
    public String getTitle() {
        return getSingleProperty("heeweb:title");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:summary")
    public HippoHtml getSummary() {
        return getHippoHtml("heeweb:summary");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:relatedNews")
    public List<HippoBean> getRelatedNews() {
        return getLinkedBeans("heeweb:relatedNews", HippoBean.class);
    }
}
