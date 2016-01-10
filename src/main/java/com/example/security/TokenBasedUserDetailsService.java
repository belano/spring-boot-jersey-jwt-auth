package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class TokenBasedUserDetailsService implements
        AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        final String authToken = (String) token.getPrincipal();
        final String username = this.tokenUtils.getUsernameFromToken(authToken);
        if (username != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (this.tokenUtils.validateToken(authToken, userDetails)) {
                return userDetails;
            }
        }
        throw new BadCredentialsException("Bad credentials");
    }

}
