package uk.nhs.hee.web.security.support.springsecurity.authentication.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import uk.nhs.hee.web.security.support.springsecurity.authentication.userdetails.service.impl.AzureADUserDetailsServiceImpl;

/**
 * <code>AuthenticationProvider</code> implementation which authenticates users
 * against Azure AD.
 */
public class AzureADAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(AzureADUserDetailsServiceImpl.class);

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        if (authentication.getCredentials() == null) {
            LOG.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(
                    messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"), null);
        }

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        final String password = authentication.getCredentials().toString();
        UserDetails loadedUser;
        try {
            loadedUser = new AzureADUserDetailsServiceImpl().loadUserByUsernameAndPassword(username, password);
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

        if (loadedUser == null) {
            throw new AuthenticationServiceException(
                    "AzureADUserDetailsServiceImpl returned null, which is an interface contract violation");
        }

        return loadedUser;
    }

}

