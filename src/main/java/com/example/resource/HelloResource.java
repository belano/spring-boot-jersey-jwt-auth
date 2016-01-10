package com.example.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.example.model.GreetingResponse;

@Component
@Path("/hello")
public class HelloResource {

    @GET
    @Produces(APPLICATION_JSON)
    public Response message(@QueryParam("name") String name) {
        String greeting = "Hello " + name;
        return Response.ok(new GreetingResponse(greeting)).build();
    }

    @Path("/error")
    @GET
    public Response error() {
        throw new RuntimeException("Some error");
    }
}
