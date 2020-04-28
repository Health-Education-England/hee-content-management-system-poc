package uk.nhs.hee.web.security.config.oauth2.azure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Configures this web app as an OAuth2 client, meaning this web app will be a
 * calling some API services secured via OAuth2. In order to call these secured
 * API services, the user needs to authenticate with the authorization server,
 * and authorize the web app to access the API services in behalf of the user.
 * </p>
 * <p>
 * With the {@link EnableOAuth2Client} annotation, this configuration uses
 * {@link OAuth2ClientConfiguration} that is provided by Spring Security OAuth2.
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@PropertySource("classpath:azure-oauth2.properties")
public class AzureOAuth2ClientSecurityConfig extends WebSecurityConfigurerAdapter {

	// This is made possible because of @EnableOAuth2Client
	// and RequestContextListener.
	@Autowired
	private OAuth2ClientContext oauth2ClientContext;

	/**
	 * <p>
	 * Handles a {@link UserRedirectRequiredException} that is thrown from
	 * {@link OAuth2ClientAuthenticationProcessingFilter}.
	 * </p>
	 * <p>
	 * This bean is configured because of <code>@EnableOAuth2Client</code>.
	 * </p>
	 */
	@Autowired
	private OAuth2ClientContextFilter oauth2ClientContextFilter;


	@Value("${clientId}")
	private String clientId;

	@Value("${clientSecret}")
	private String clientSecret;

	@Value("${userAuthorizationUri}")
	private String userAuthorizationUri;

	@Value("${accessTokenUri}")
	private String accessTokenUri;

	@Value("${tokenName:authorization_code}")
	private String tokenName;

	@Value("${scope}")
	private String scope;

	@Value("${userInfoUri}")
	private String userInfoUri;

	@Value("${filterCallbackPath}")
	private String oauth2FilterCallbackPath;


	private OAuth2ProtectedResourceDetails authorizationCodeResource() {
		AuthorizationCodeResourceDetails authCodeResourceDetails = new AuthorizationCodeResourceDetails();
		authCodeResourceDetails.setId("azure-oauth2-client");
		authCodeResourceDetails.setClientId(clientId);
		authCodeResourceDetails.setClientSecret(clientSecret);
		authCodeResourceDetails.setUserAuthorizationUri(userAuthorizationUri);
		authCodeResourceDetails.setAccessTokenUri(accessTokenUri);
		authCodeResourceDetails.setTokenName(tokenName);
		String commaSeparatedScopes = scope;
		authCodeResourceDetails.setScope(parseScopes(commaSeparatedScopes));

		// Defaults to use current URI
		/*
		 * If a pre-established redirect URI is used, it will need to be an
		 * absolute URI. To do so, it'll need to compute the URI from a
		 * request. The HTTP request object is available when you override
		 * OAuth2ClientAuthenticationProcessingFilter#attemptAuthentication().
		 *
		 * details.setPreEstablishedRedirectUri(
		 * 		env.getProperty("redirectUrl"));
		 * details.setUseCurrentUri(false);
		 */
		authCodeResourceDetails.setAuthenticationScheme(AuthenticationScheme.query);
		authCodeResourceDetails.setClientAuthenticationScheme(AuthenticationScheme.form);
		return authCodeResourceDetails;
	}

	private List<String> parseScopes(String commaSeparatedScopes) {
		List<String> scopes = new LinkedList<>();
		Collections.addAll(scopes, commaSeparatedScopes.split(","));
		return scopes;
	}

	/**
	 * @return an OAuth2 client authentication processing filter
	 */
	@Bean
	@Description("Filter that checks for authorization code, "
			+ "and if there's none, acquires it from authorization server")
	public OAuth2ClientAuthenticationProcessingFilter
				oauth2ClientAuthenticationProcessingFilter() {
		// Used to obtain access token from authorization server (AS)
		OAuth2RestOperations restTemplate = new OAuth2RestTemplate(
				authorizationCodeResource(),
				oauth2ClientContext);
		OAuth2ClientAuthenticationProcessingFilter filter =
				new OAuth2ClientAuthenticationProcessingFilter(oauth2FilterCallbackPath);
		filter.setRestTemplate(restTemplate);

		// Set a service that validates an OAuth2 access token
		// For this, we chose to use UserInfoTokenServices
		filter.setTokenServices(new UserInfoTokenServices(userInfoUri, clientId));
		filter.setAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler());
		return filter;
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		// May need an OAuth2AuthenticationEntryPoint for non-browser clients
		return new LoginUrlAuthenticationEntryPoint(oauth2FilterCallbackPath);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webfiles/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint())
			.and()
				.authorizeRequests().anyRequest().authenticated()
			.and()
				.logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
			/* No need for form-based login or basic authentication
			.and()
				.formLogin()
					.loginPage("...")
					.loginProcessingUrl("...")
			.and()
				.httpBasic()
			 */
			.and()
				.addFilterAfter(
					oauth2ClientContextFilter,
					ExceptionTranslationFilter.class)
				.addFilterBefore(
					oauth2ClientAuthenticationProcessingFilter(),
					FilterSecurityInterceptor.class)
				.anonymous()
				// anonymous login must be disabled,
				// otherwise an anonymous authentication will be created,
				// and the UserRedirectRequiredException will not be thrown,
				// and the user will not be redirected to the authorization server
					.disable();
	}

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return new NoopAuthenticationManager();
	}

	private static class NoopAuthenticationManager implements AuthenticationManager {
		@Override
		public Authentication authenticate(Authentication authentication)
				throws AuthenticationException {
			throw new UnsupportedOperationException(
					"No authentication should be done with this AuthenticationManager");
		}
	}

	@Bean
	@Description("Enables ${...} expressions in the @Value annotations"
			+ " on fields of this configuration. Not needed if one is"
			+ " already available.")
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
