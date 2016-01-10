package com.example.config;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.example.resource.AuthResource;
import com.example.resource.HelloResource;

@Component
@ApplicationPath("/")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        packages("com.example");
        // packages("com.example.provider");
        register(HelloResource.class);
        register(AuthResource.class);
    }
}
