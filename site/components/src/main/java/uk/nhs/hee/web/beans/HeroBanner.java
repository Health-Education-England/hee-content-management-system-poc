package uk.nhs.hee.web.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;

@HippoEssentialsGenerated(internalName = "heeweb:heroBanner")
@Node(jcrType = "heeweb:heroBanner")
public class HeroBanner extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "heeweb:heading")
    public String getHeading() {
        return getSingleProperty("heeweb:heading");
    }

    @HippoEssentialsGenerated(internalName = "heeweb:summaryText")
    public String getSummaryText() {
        return getSingleProperty("heeweb:summaryText");
    }
}
