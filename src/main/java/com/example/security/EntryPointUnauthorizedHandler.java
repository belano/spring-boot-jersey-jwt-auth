package com.example.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Returns a 401 error code (Unauthorized) to the client.
 */
@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(EntryPointUnauthorizedHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        logger.debug("AuthenticationException: {}", exception.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try (PrintWriter out = response.getWriter()) {
            out.print("{\"message\":\"Full authentication is required to access this resource.\", \"access-denied\":true,\"cause\":\"NOT AUTHENTICATED\"}");
            out.flush();
        } catch (IOException e) {
            logger.warn("Unexpected IO exception", e);
        }
    }

}
