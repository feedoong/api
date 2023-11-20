package io.feedoong.api.global.security.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String jwtToken;

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, String jwtToken) {
        super(authorities);
        this.jwtToken = jwtToken;
    }

    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
