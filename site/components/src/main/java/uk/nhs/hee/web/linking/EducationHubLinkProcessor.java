package uk.nhs.hee.web.linking;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.linking.HstLinkProcessorTemplate;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EducationHubLinkProcessor extends HstLinkProcessorTemplate {
    public static final List<String> EDUCATION_HUB_CHANNELS =
            Collections.singletonList("medical-education-hub");

    public static final String EDUCATION_HUB_SITE_MAP_ROOT = "root";
    public static final String EDUCATION_HUB_FOLDER_PATH_HUB = "hub";
    public static final String EDUCATION_HUB_SITE_MAP_ROOT_PLUS_HUB =
            EDUCATION_HUB_SITE_MAP_ROOT + "/" + EDUCATION_HUB_FOLDER_PATH_HUB;
    public static final String EDUCATION_HUB_SUB_HUB_SUFFIX = "subhub";

    /**
     * Returns the processed link for link creation
     * for the <I>Education Hub</I> (e.g. <I>medical-education-hub</I>) channels.
     *
     * <p>It essentially removes the following from the link path:</p>
     * <ul>
     *     <li>
     *         Removes <I>/root</I> path from the link.
     *     </li>
     *     <li>
     *         Removes <I>subhub</I> suffix from the subhub folder paths.
     *     </li>
     * </ul>
     *
     * <p>Example: rewrites <I>root/hub/trainingsubhub/specialtysubhub/surgerysubhub/
     * paediatric/paediatric-surgery-at-west-midlands</I> to <I>hub/training/specialty/surgery/
     * paediatric/paediatric-surgery-at-west-midlands</I></p>
     *
     * @param link the {@link HstLink} instance
     * @return the processed link for link creation.
     */
    @Override
    protected HstLink doPostProcess(HstLink link) {
        if (isNotEducationHubChannel(link.getMount())
                || !link.getPath().startsWith(EDUCATION_HUB_SITE_MAP_ROOT_PLUS_HUB)
                || link.getPath().contains("_default_")) {
            return link;
        }

        String processedPath = link.getPath()
                .replace(EDUCATION_HUB_SITE_MAP_ROOT_PLUS_HUB, EDUCATION_HUB_FOLDER_PATH_HUB)
                .replaceAll(EDUCATION_HUB_SUB_HUB_SUFFIX + "/", "/");

        link.setPath(processedPath);

        return link;
    }

    /**
     * Returns the processed link for link creation
     * for the <I>Education Hub</I> (e.g. <I>medical-education-hub</I>) channels.
     *
     * <p>It essentially does the inverse of
     * {@link EducationHubLinkProcessor#doPostProcess(org.hippoecm.hst.core.linking.HstLink)}:</p>
     * <ul>
     *     <li>
     *         Prefixes the link path with <I>/root</I>.
     *     </li>
     *     <li>
     *         Adds <I>subhub</I> suffixes to the subhub folder paths.
     *     </li>
     * </ul>
     *
     * <p>Example: rewrites <I>hub/training/specialty/surgery/paediatric/
     * paediatric-surgery-at-west-midlands</I> to <I>root/hub/trainingsubhub/specialtysubhub/
     * surgerysubhub/paediatric/paediatric-surgery-at-west-midlands</I></p>
     *
     * @param link the {@link HstLink} instance
     * @return the processed link for link matching.
     */
    @Override
    protected HstLink doPreProcess(HstLink link) {
        Mount mount = link.getMount();

        if (isNotEducationHubChannel(mount)
                || !link.getPath().startsWith(EDUCATION_HUB_FOLDER_PATH_HUB)) {
            return link;
        }

        String mountPath = mount.getMountPath();
        String processedPath = EDUCATION_HUB_SITE_MAP_ROOT + "/" + link.getPath();
        processedPath = suffixSubHubPaths(mountPath, processedPath);

        link.setPath(processedPath);

        return link;
    }

    private String suffixSubHubPaths(String mountPath, String processedPath) {
        Pattern subHubPathPattern = Pattern.compile(EDUCATION_HUB_SITE_MAP_ROOT_PLUS_HUB + "/(.*)?/");
        Matcher subHubPathMatcher = subHubPathPattern.matcher(processedPath);
        if (subHubPathMatcher.find()) {
            String subHubPath = subHubPathMatcher.group(1) + "/";
            processedPath = processedPath
                    .replace(subHubPath,
                            subHubPath.replaceAll("/", EDUCATION_HUB_SUB_HUB_SUFFIX + "/"));
        }
        return processedPath;
    }

    private boolean isNotEducationHubChannel(Mount mount) {
        return !EDUCATION_HUB_CHANNELS.contains(mount.getChannel().getId());
    }
}
