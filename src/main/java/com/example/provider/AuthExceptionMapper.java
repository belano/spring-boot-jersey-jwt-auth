package com.example.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

@Provider
public class AuthExceptionMapper implements ExceptionMapper<AuthenticationException> {

    private static final Logger logger = LoggerFactory.getLogger(AuthExceptionMapper.class);

    @Override
    public Response toResponse(AuthenticationException exception) {
        logger.debug("Exception: {}", exception.getMessage());
        return Response.status(Status.UNAUTHORIZED).entity(exception.getMessage()).type("text/plain").build();
    }

}
