package uk.nhs.hee.web.component.helper;

import org.apache.commons.collections.IteratorUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.web.beans.HubDocument;
import uk.nhs.hee.web.beans.ProgrammeDocument;

import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

public class TrainingHubHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingHubHelper.class);

    public static List<HippoBean> getSubHubs(HstRequest request) {
        HippoBean subHubFolderHippoBean = request.getRequestContext()
                .getContentBean()
                .getParentBean()
                .getBean(getHubDocumentBean(request).getSubHubType().toLowerCase() + "subhub");

        if (subHubFolderHippoBean == null) {
            return Collections.emptyList();
        }


        return subHubFolderHippoBean.getChildBeans("heeweb:hubDocument");
    }

    public static List<HubDocument> getParentHubDocuments(HstRequest request) {
        HippoBean folderBean = request.getRequestContext().getContentBean().getParentBean();
        List<HubDocument> hubDocuments = new ArrayList<>();

        HippoBean parentFolderBean = null;
        String subHubType = null;
        try {
            while (!"hub".equalsIgnoreCase(folderBean.getNode().getName())) {
                parentFolderBean = folderBean.getParentBean();
                subHubType = folderBean.getNode().getName().replace("subhub", "");
                HubDocument hubDocument = getHubDocument(parentFolderBean, subHubType);

                if (hubDocument == null) {
                    folderBean = parentFolderBean;
                    continue;
                }

                hubDocuments.add(hubDocument);
                folderBean = parentFolderBean;
            }
        } catch (RepositoryException e) {
            LOGGER.error(
                    "Caught error '{}' while getting HubDocument with subtype '{}' under '{}'",
                    e.getMessage(),
                    subHubType,
                    parentFolderBean != null ? parentFolderBean.getPath() : "",
                    e);
        }

        Collections.reverse(hubDocuments);

        return hubDocuments;
    }

    public static HubDocument getHubDocumentBean(HstRequest request) {
        return request.getRequestContext().getContentBean(HubDocument.class);
    }

    public static HubDocument getSubSpecialtyDocument(ProgrammeDocument programmeDocument) {
        HippoBean parentFolderBean = programmeDocument.getParentBean();
        HippoBean grandParentFolderBean = parentFolderBean.getParentBean();

        return getHubDocument(grandParentFolderBean, parentFolderBean.getName());
    }

    public static HubDocument getTrainingTypeDocument(ProgrammeDocument programmeDocument) {
        HippoBean greatGrandParentFolderBean = programmeDocument.getParentBean().getParentBean().getParentBean();
        HippoBean greatGreatGrandParentFolderBean = greatGrandParentFolderBean.getParentBean();

        return getHubDocument(greatGreatGrandParentFolderBean, greatGrandParentFolderBean.getName());
    }

    private static HubDocument getHubDocument(HippoBean scopeBean, String subHubType) {
        HstQuery hstQuery = HstQueryBuilder.create(scopeBean)
                .ofTypes("heeweb:hubDocument")
                .where(constraint("heeweb:subHubType")
                        .equalToCaseInsensitive(subHubType.replace("subhub", "")))
                .build();
        try {
            HstQueryResult result = hstQuery.execute();
            @SuppressWarnings("unchecked") List<HubDocument> subSpecialtyDocumentList =
                    IteratorUtils.toList(result.getHippoBeans());

            if (subSpecialtyDocumentList.size() == 1) {
                return subSpecialtyDocumentList.get(0);
            }
        } catch (QueryException e) {
            LOGGER.error(
                    "Caught error '{}' while querying all programme pages ('programmeDocument') under '{}'",
                    e.getMessage(),
                    scopeBean.getPath(),
                    e);
        }

        return null;
    }
}
