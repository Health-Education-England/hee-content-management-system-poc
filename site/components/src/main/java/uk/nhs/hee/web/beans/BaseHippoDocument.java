package uk.nhs.hee.web.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;

/** 
 * Interface for documents which can be (un)published to search engines.
 */
@HippoEssentialsGenerated(internalName = "heeweb:BaseHippoDocument")
@Node(jcrType = "heeweb:BaseHippoDocument")
public class BaseHippoDocument extends BaseDocument {

    @HippoEssentialsGenerated(internalName = "heeweb:title")
    public String getTitle() {
        return getSingleProperty("heeweb:title");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:introduction")
    public String getIntroduction() {
        return getSingleProperty("heeweb:introduction");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:content")
    public String getContent() {
        return getSingleProperty("heeweb:content");
    }
}
