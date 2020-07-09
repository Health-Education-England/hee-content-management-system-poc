package uk.nhs.hee.web.component;

import org.apache.commons.collections.IteratorUtils;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.hee.web.beans.HubDocument;
import uk.nhs.hee.web.beans.ProgrammeDocument;
import uk.nhs.hee.web.beans.ProgrammeForListing;

import java.util.List;
import java.util.stream.Collectors;

public class ProgrammeListingComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammeListingComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        HubDocument hubDocument =
                request.getRequestContext().getContentBean(HubDocument.class);

        request.setModel("programmeListingTitle", hubDocument.getTitle() + " programmes");

        addSpecialtyDocsToModel(request, hubDocument.getSubHubType());
    }

    private void addSpecialtyDocsToModel(HstRequest request, String subHubType) {
        List<HippoBean> subHubTypeFolderBean = request.getRequestContext()
                .getContentBean()
                .getParentBean()
                .getChildBeansByName(subHubType.toLowerCase() + "subhub");

        if (subHubTypeFolderBean == null || subHubTypeFolderBean.size() != 1) {
            return;
        }

        HstQuery hstQuery = HstQueryBuilder.create(subHubTypeFolderBean.get(0))
                .ofTypes("heeweb:programmeDocument")
                .orderByAscending("heeweb:region")
                .build();
        try {
            HstQueryResult result = hstQuery.execute();
            @SuppressWarnings("unchecked") List<ProgrammeDocument> programmeBeanList =
                    IteratorUtils.toList(result.getHippoBeans());

            List<ProgrammeForListing> programmesForListing = programmeBeanList.stream()
                    .map(programmeDocument -> {
                        return new ProgrammeForListing(request, programmeDocument);
                    })
                    .collect(Collectors.toList());

            request.setModel("programmes", programmesForListing);
        } catch (QueryException e) {
            LOGGER.error(
                    "Caught error '{}' while querying programmes ('programmeDocument') under '{}'",
                    e.getMessage(),
                    subHubTypeFolderBean.get(0).getPath(),
                    e);
        }
    }

}
