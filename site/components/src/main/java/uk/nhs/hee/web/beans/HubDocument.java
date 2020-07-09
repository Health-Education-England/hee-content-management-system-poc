package uk.nhs.hee.web.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import java.util.List;
import uk.nhs.hee.web.beans.Section;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;

@HippoEssentialsGenerated(internalName = "heeweb:hubDocument")
@Node(jcrType = "heeweb:hubDocument")
public class HubDocument extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "heeweb:title")
    public String getTitle() {
        return getSingleProperty("heeweb:title");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:category")
    public String getCategory() {
        return getSingleProperty("heeweb:category");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:abstract")
    public String getAbstract() {
        return getSingleProperty("heeweb:abstract");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:subHubType")
    public String getSubHubType() {
        return getSingleProperty("heeweb:subHubType");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:sections")
    public List<Section> getSections() {
        return getChildBeansByName("heeweb:sections", Section.class);
    }

    @HippoEssentialsGenerated(internalName = "heeweb:heroBanner")
    public HippoBean getHeroBanner() {
        return getLinkedBean("heeweb:heroBanner", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "heeweb:image")
    public HippoGalleryImageSet getImage() {
        return getLinkedBean("heeweb:image", HippoGalleryImageSet.class);
    }
}
