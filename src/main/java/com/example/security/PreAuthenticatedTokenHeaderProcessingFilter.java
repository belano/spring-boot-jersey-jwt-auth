package com.example.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

public class PreAuthenticatedTokenHeaderProcessingFilter extends RequestHeaderAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(PreAuthenticatedTokenHeaderProcessingFilter.class);

    private String principalRequestHeader;

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
            final Authentication authResult) {
        logger.debug("successfulAuthentication: {}", authResult);

        String tokenKey = request.getHeader(principalRequestHeader);
        response.addHeader(principalRequestHeader, tokenKey);

        super.successfulAuthentication(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) {
        super.unsuccessfulAuthentication(request, response, failed);

        logger.debug("unsuccessfulAuthentication: {}", failed.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try (PrintWriter out = response.getWriter()) {
            out.print("{\"message\":\"Full authentication is required to access this resource.\", \"access-denied\":true,\"cause\":\"BAD CREDENTIALS\"}");
            out.flush();
        } catch (IOException e) {
            logger.warn("Unexpected IO exception", e);
        }
    }

    @Override
    public void setPrincipalRequestHeader(final String principalRequestHeader) {
        this.principalRequestHeader = principalRequestHeader;
        super.setPrincipalRequestHeader(principalRequestHeader);
    }
}
