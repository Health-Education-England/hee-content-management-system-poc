package uk.nhs.hee.web.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;

@HippoEssentialsGenerated(internalName = "heeweb:staff")
@Node(jcrType = "heeweb:staff")
public class Staff extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "heeweb:name")
    public String getName() {
        return getSingleProperty("heeweb:name");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:title")
    public String getTitle() {
        return getSingleProperty("heeweb:title");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:department")
    public String getDepartment() {
        return getSingleProperty("heeweb:department");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:image")
    public HippoGalleryImageSet getImage() {
        return getLinkedBean("heeweb:image", HippoGalleryImageSet.class);
    }
}
