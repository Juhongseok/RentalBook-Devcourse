package com.jhs.rentbook.integration.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AbstractIntegrationTest {

    private static final String HEADER_NAME = "LOGIN-ID";

    @LocalServerPort
    private int port;

    protected String baseUrl = "";

    @Autowired
    protected TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:".concat(port + "");
    }

    protected  <T> HttpEntity<T> convertHttpEntity(T request, String headerValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_NAME, headerValue);

        return new HttpEntity<>(request, headers);
    }

}
