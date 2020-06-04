package uk.nhs.hee.web.rest.services;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.algolia.search.models.indexing.BatchIndexingResponse;
import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.jaxrs.services.AbstractResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import uk.nhs.hee.web.beans.BaseHippoDocument;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@PropertySource("classpath:algolia.properties")
@Path("/algolia/update/")
public class AlgoliaUpdateResource extends AbstractResource {
    public static final String INDEX_ACTION = "index";
    private static final Logger log = LoggerFactory.getLogger(AlgoliaUpdateResource.class);

    private final SearchClient client;
    private final SearchIndex<BaseHippoContent> index;

    @Value("${algolia.application.id}")
    private String applicationId;

    @Value("${algolia.api.key}")
    private String apiKey;

    public AlgoliaUpdateResource() {
       this.client =
                DefaultSearchClient.create("UDB415NOQA", "f17388442e4a195de258094b0ce36c96");

       this.index = client.initIndex("brdocs", BaseHippoContent.class);
    }

    @POST
    @Path("/")
    public Response update(@QueryParam("action") @DefaultValue(INDEX_ACTION) String action, @QueryParam("id") String handleUuid) {

        log.info("Updating ('{}') document from search index: {}", action, handleUuid);

        if (StringUtils.isNotEmpty(handleUuid)) {
            try {
                HstRequestContext requestContext = RequestContextProvider.get();
                log.info("Request context" + requestContext);
                if (INDEX_ACTION.equals(action)) {

                    Node node = requestContext.getSession().getNodeByIdentifier(handleUuid);

                    log.info("Node" + node);
                    HippoBean bean = (HippoBean) getObjectConverter(requestContext).getObject(node);

                    if (bean instanceof BaseHippoDocument) {
                        BaseHippoDocument document = (BaseHippoDocument) bean;
                        log.info("document taken " + document.getTitle());
                        BaseHippoContent baseHippoContent = new BaseHippoContent();
                        baseHippoContent.setContent(document.getContent());
                        baseHippoContent.setIntroduction(document.getIntroduction());
                        baseHippoContent.setTitle(document.getIntroduction());
                        baseHippoContent.setObjectID(document.getCanonicalUUID());

                        BatchIndexingResponse response = index.saveObject(baseHippoContent);
                        log.info("algolia resp" + response.toString());

                        return Response.status(200).entity("Sucess Yeey!").build();

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

