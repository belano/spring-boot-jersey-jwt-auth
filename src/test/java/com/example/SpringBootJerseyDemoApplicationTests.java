package com.example;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.model.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootJerseyDemoApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class SpringBootJerseyDemoApplicationTests {
    
    private static final Logger logger = LoggerFactory.getLogger(SpringBootJerseyDemoApplicationTests.class);

    @Value("${local.server.port}")
    int port;

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void greeting_accessible_with_token() throws Exception {
        AuthenticationRequest authRequest = new AuthenticationRequest("user", "password");

        // @formatter:off
        Response tokenResponse = given()
            .contentType("application/json")
            .body(authRequest)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .extract().response();
        // @formatter:on

        String token = tokenResponse.getBody().jsonPath().getString("token");
        logger.debug("Token: {}", token);

        // @formatter:off
        given()
            .header(new Header("X-Auth-Token", token))
            .param("name", "jdoe")
        .when()
            .get("/hello")
        .then()
            .contentType("application/json")
            .statusCode(HttpStatus.SC_OK)
            .body("greeting", equalTo("Hello jdoe"));
        // @formatter:on
    }

    @Test
    public void greeting_requires_auth() {
        // @formatter:off
        when()
            .get("/hello")
        .then()
            .statusCode(HttpStatus.SC_UNAUTHORIZED);
        // @formatter:on
    }
    
    @Test
    public void greeting_bad_token() {
        // @formatter:off
        given()
            .header(new Header("X-Auth-Token", "token"))
            .param("name", "jdoe")
        .when()
            .get("/hello")
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED);
        // @formatter:on
    }

}
