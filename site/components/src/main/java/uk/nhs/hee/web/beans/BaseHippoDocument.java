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

    @HippoEssentialsGenerated(internalName = "heeweb:category")
    public String[] getCategory() {
        return getMultipleProperty("heeweb:category");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:region")
    public String getRegion() {
        return getSingleProperty("heeweb:region");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:speciality")
    public String getSpeciality() {
        return getSingleProperty("heeweb:speciality");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:subspeciality")
    public String getSubspeciality() {
        return getSingleProperty("heeweb:subspeciality");
    }
}
