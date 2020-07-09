package uk.nhs.hee.web.component.helper;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.web.beans.HubDocument;

import java.util.List;
import java.util.stream.Collectors;

public class DocumentHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentHelper.class);

    public static String getSiteContextUrl(HstRequest request, HippoBean documentHippoBean) {
        HstRequestContext requestContext = request.getRequestContext();
        HstLinkCreator linkCreator = requestContext.getHstLinkCreator();

        List<HstLink> links = linkCreator.createAllAvailableCanonicals(documentHippoBean.getNode(), requestContext)
                .stream()
                .filter(link -> !link.toUrlForm(requestContext, false).contains("/resourceapi"))
                .collect(Collectors.toList());

        if (links.size() == 1) {
            return links.get(0).toUrlForm(requestContext, false);
        }

        return StringUtils.EMPTY;
    }

    public static HippoBean getDocumentBean(HstRequest request, String documentRootPath, String documentRelativePath) {
        HippoBean rootHippoBean = null;

        try {
            rootHippoBean = (HippoBean) request.getRequestContext()
                    .getObjectBeanManager().getObject(documentRootPath);
        } catch (ObjectBeanManagerException e) {
            LOGGER.error(
                    "Caught error '{}' while retrieving the object/bean for the path '{}'",
                    e.getMessage(),
                    documentRootPath,
                    e);
            return null;
        }

        return rootHippoBean.getBean(documentRelativePath, HubDocument.class);
    }

}
