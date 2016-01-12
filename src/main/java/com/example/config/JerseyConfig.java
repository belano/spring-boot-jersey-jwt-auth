package com.example.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.example.provider.AuthExceptionMapper;
import com.example.provider.GenericExceptionMapper;
import com.example.provider.ObjectMapperProvider;
import com.example.resource.AuthResource;
import com.example.resource.HelloResource;

@Component
@ApplicationPath("/")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
//        packages("com.example");
        register(HelloResource.class);
        register(AuthResource.class);
        register(ObjectMapperProvider.class);
        register(AuthExceptionMapper.class);
        register(GenericExceptionMapper.class);
        register(LoggingFilter.class);
    }
}
