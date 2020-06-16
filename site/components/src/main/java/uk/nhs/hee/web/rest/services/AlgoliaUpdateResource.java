package uk.nhs.hee.web.rest.services;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.BatchIndexingResponse;
import com.algolia.search.models.settings.IndexSettings;
import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.jaxrs.services.AbstractResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import uk.nhs.hee.web.beans.Article;
import uk.nhs.hee.web.rest.services.entity.AlgoliaArticle;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;

@PropertySource("classpath:algolia.properties")
@Path("/algolia/update/")
public class AlgoliaUpdateResource extends AbstractResource {
    public static final String INDEX_ACTION = "index";
    private static final Logger log = LoggerFactory.getLogger(AlgoliaUpdateResource.class);

    private final SearchClient client;
    private final SearchIndex<AlgoliaArticle> index;

    public AlgoliaUpdateResource(String applicationId, String apiKey) {
        this.client = DefaultSearchClient.create(applicationId, apiKey);
        this.index = this.client.initIndex("articles", AlgoliaArticle.class);
        this.index.setSettings(
                new IndexSettings()
                        .setAttributesForFaceting(Arrays.asList(
                                "category",
                                "region",
                                "speciality",
                                "subSpeciality"
                        ))
                        .setSearchableAttributes(
                                Arrays.asList(
                                        "summary",
                                        "introduction",
                                        "title"
                                )
                        )
                        .setAttributesToSnippet(
                                Arrays.asList(
                                        "summary:80",
                                        "introduction:40",
                                        "title:40"
                                )
                        )
                        .setSnippetEllipsisText("…")
                        .setReplicas(Collections.singletonList(
                                "articles_date_desc"
                        )),
                true
        );
        SearchIndex<AlgoliaArticle> replicaIndex = this.client.initIndex("articles_date_desc", AlgoliaArticle.class);
        replicaIndex.setSettings(
                new IndexSettings().setRanking(Arrays.asList(
                        "desc(lastUpdateAt)",
                        "typo",
                        "geo",
                        "words",
                        "filters",
                        "proximity",
                        "attribute",
                        "exact",
                        "custom"
                ))
        );
    }

    @POST
    @Path("/")
    public Response update(@QueryParam("action") @DefaultValue(INDEX_ACTION) String action, @QueryParam("id") String handleUuid) {

        log.info("Updating ('{}') document from search index: {}", action, handleUuid);

        if (StringUtils.isNotEmpty(handleUuid)) {
            try {
                HstRequestContext requestContext = RequestContextProvider.get();
                if (INDEX_ACTION.equals(action)) {

                    Node node = requestContext.getSession().getNodeByIdentifier(handleUuid);
                    HippoBean bean = (HippoBean) getObjectConverter(requestContext).getObject(node);

                    if (bean instanceof Article) {
                        Article document = (Article) bean;
                        AlgoliaArticle baseHippoContent = createPayload(document);
                        BatchIndexingResponse response = this.index.saveObject(baseHippoContent);
                        return Response.status(200).entity("Sucess!").build();

                    } else {
                        log.warn("The bean from '{}' is not a BaseHippoDocument.", handleUuid);
                    }

                } else {
                    log.warn("Unknown action: '{}'.", action);
                }
            } catch (ItemNotFoundException e) {
                log.warn("The news is not found by the identifier: '{}'", handleUuid);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.warn("Failed to find news by identifier.", e);
                } else {
                    log.warn("Failed to find news by identifier. {}", e.toString());
                }

                throw new WebApplicationException(e, buildServerErrorResponse(e));
            }
        }

        return Response.ok().build();
    }

    private String getPath(Article article) {
        HstRequestContext requestContext = RequestContextProvider.get();
        HstLinkCreator linkCreator = requestContext.getHstLinkCreator();
        HstLink link = linkCreator.create(article, requestContext);
        return link.getPath();
    }

    private AlgoliaArticle createPayload(Article article) {
        AlgoliaArticle algoliaArticle = new AlgoliaArticle();
        algoliaArticle.setSummary(article.getSummary().getContent());
        algoliaArticle.setTitle(article.getTitle());
        algoliaArticle.setCategory(article.getCategory());
        algoliaArticle.setRegion(article.getRegion());
        algoliaArticle.setSpeciality(article.getSpeciality());
        algoliaArticle.setSubSpeciality(article.getSubspeciality());
        Long updateTimestamp =
                ((GregorianCalendar) article.getSingleProperty("hippostdpubwf:lastModificationDate"))
                        .getTimeInMillis();
        algoliaArticle.setLastUpdateAt(updateTimestamp);
        algoliaArticle.setObjectID(article.getCanonicalUUID());
        algoliaArticle.setPath(this.getPath(article));

        return algoliaArticle;
    }

    /**
     * Creates a generic JAX-RS Error Response from the given error.
     *
     * @param th
     * @return
     */
    protected Response buildServerErrorResponse(Throwable th) {
        return Response.serverError().entity(th.getCause() != null ? th.getCause().toString() : th.toString()).build();
    }
}

