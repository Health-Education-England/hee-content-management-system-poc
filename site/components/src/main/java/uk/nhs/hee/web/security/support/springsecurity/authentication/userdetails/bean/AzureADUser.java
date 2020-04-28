package uk.nhs.hee.web.security.support.springsecurity.authentication.userdetails.bean;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AzureADUser extends User {

    private static final long serialVersionUID = 1L;
    private final String accessToken;

    public AzureADUser(String username, String password, Collection<? extends GrantedAuthority> authorities, final String accessToken) {
        this(username, password, true, true, true, true, authorities, accessToken);
    }

    public AzureADUser(String username, String password, boolean enabled, boolean accountNonExpired,
                       boolean credentialsNonExpired, boolean accountNonLocked,
                       Collection<? extends GrantedAuthority> authorities, final String accessToken) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

}