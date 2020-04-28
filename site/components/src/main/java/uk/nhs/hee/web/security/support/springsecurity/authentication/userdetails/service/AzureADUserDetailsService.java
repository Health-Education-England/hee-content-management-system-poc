package uk.nhs.hee.web.security.support.springsecurity.authentication.userdetails.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Interface extending <code>AzureADUserDetailsService</code>
 * for integrating with Azure AD.
 */
public interface AzureADUserDetailsService extends UserDetailsService {

    /**
     * Load <code>UserDetails</code> by username and password.
     *
     * @param username username
     * @param password password
     * @return UserDetails instance
     * @throws UsernameNotFoundException if user is not found
     */
    UserDetails loadUserByUsernameAndPassword(String username, String password) throws UsernameNotFoundException;

}
