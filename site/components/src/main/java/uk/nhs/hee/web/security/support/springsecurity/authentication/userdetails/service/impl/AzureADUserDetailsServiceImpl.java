package uk.nhs.hee.web.security.support.springsecurity.authentication.userdetails.service.impl;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.nhs.hee.web.security.support.springsecurity.authentication.userdetails.bean.AzureADUser;
import uk.nhs.hee.web.security.support.springsecurity.authentication.userdetails.service.AzureADUserDetailsService;

public class AzureADUserDetailsServiceImpl implements AzureADUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(AzureADUserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByUsernameAndPassword(username, null);
    }

    @Override
    public UserDetails loadUserByUsernameAndPassword(String username, String password)
            throws AuthenticationServiceException {
        LOG.debug("Username -> {}, Password -> {}", username, "*****");

        final String accessToken = getAccessToken(username, password);

        if (StringUtils.isEmpty(accessToken)) {
            throw new AuthenticationServiceException(
                    "Could not authenticate user " + username + " against the Azure AD");
        }

        // Collection<? extends GrantedAuthority> authorities = getGrantedAuthoritiesOfUser(repoUser);
        return new AzureADUser(username, password, Collections.emptyList(), accessToken);
    }

    protected String getAccessToken(final String username, final String password) {
        String accessToken = null;

        final RestTemplate restTemplate = new RestTemplate();

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>() {
            private static final long serialVersionUID = 1L;
            {
                add("grant_type", "password");
                add("client_id", "<client_id>");
                add("client_secret", "<client_secret>");
                add("scope", "https://graph.microsoft.com/.default");
                add("userName", username);
                add("password", password);
            }};

        final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, null);

        final ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://login.microsoftonline.com/9a06357f-10b7-4a26-be05-156f27ee0bd2/oauth2/v2.0/token",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map<String, String> responseObject;
            try {
                responseObject = new ObjectMapper()
                        .readValue(responseEntity.getBody(), new TypeReference<Map<String, String>>() {});
                accessToken = responseObject.get("access_token");
            } catch (JsonProcessingException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        LOG.debug("accessToken -> {}", accessToken);

        return accessToken;
    }
}
