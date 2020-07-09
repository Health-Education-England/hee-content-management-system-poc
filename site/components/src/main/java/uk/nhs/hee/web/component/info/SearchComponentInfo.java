package uk.nhs.hee.web.component.info;

import org.hippoecm.hst.core.parameters.Parameter;

public interface SearchComponentInfo {
    @Parameter(
            name = "appId",
            required = true,
            displayName = "Algolia App Id"
    )
    String getAppId();

    @Parameter(
            name = "apiKey",
            required = true,
            displayName = "Algolia Api Key"
    )
    String getApiKey();

    @Parameter(
            name = "indexName",
            required = true,
            displayName = "Algolia Index Name"
    )
    String getIndexName();
}
