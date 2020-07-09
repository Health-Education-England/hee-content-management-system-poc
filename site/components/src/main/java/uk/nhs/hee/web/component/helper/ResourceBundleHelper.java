package uk.nhs.hee.web.component.helper;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.resourcebundle.ResourceBundleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class containing {@link ResourceBundle} helper methods.
 */
public class ResourceBundleHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBundleHelper.class);

    public static void addSubHubListingTitleToModel(HstRequest request) {

        addValueToModel(
                request,
                "medical-education-hub.subhub",
                TrainingHubHelper.getHubDocumentBean(request).getSubHubType().toLowerCase() + ".subhub.listing.title",
                "subHubListingTitle");
    }

    public static void addValueToModel(
            HstRequest request,
            String basename,
            String key,
            String modelKey) {
        ResourceBundle resourceBundle = ResourceBundleUtils.getBundle(basename, null);

        if (resourceBundle == null) {
            return;
        }

        try {
            request.setModel(
                    modelKey,
                    resourceBundle.getString(key));
        } catch (MissingResourceException e) {
            LOGGER.error("Caught error '{}' while reading value/message for the key '{}'", e.getMessage(), key, e);
        }
    }

}
