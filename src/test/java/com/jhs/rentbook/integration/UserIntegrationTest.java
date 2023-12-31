package com.jhs.rentbook.integration;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.user.LoginRequest;
import com.jhs.rentbook.controller.dto.user.SignUpRequest;
import com.jhs.rentbook.controller.dto.user.SignUpResponse;
import com.jhs.rentbook.controller.dto.user.UserInfo;
import com.jhs.rentbook.global.exception.ExceptionResponse;
import com.jhs.rentbook.integration.base.AbstractIntegrationTest;
import com.jhs.rentbook.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UserIntegrationTest extends AbstractIntegrationTest {

    @Test
    @Order(1)
    @DisplayName("create User 10")
    void createUsers() {
        List<SignUpResponse> responses = bulkCreateUser();

        assertThat(responses).hasSize(10);
    }

    @Test
    @Order(2)
    @DisplayName("Load All Users")
    void loadAllUsers() {
        HttpEntity<Object> entity = convertHttpEntity(null, "1");

        ResponseEntity<List> responseEntity = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, entity, List.class);
        List<UserInfo> response = responseEntity.getBody();

        assertThat(response).hasSize(11);
    }

    @Test
    @Order(3)
    @DisplayName("Fail Load All Users Has no UserId in Repository")
    void failLoadAllUsers() {
        HttpEntity<Object> entity = convertHttpEntity(null, "1000");

        ResponseEntity<ExceptionResponse> responseEntity = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, entity, ExceptionResponse.class);
        ExceptionResponse response = responseEntity.getBody();

        assertThat(response.statusCode()).isEqualTo(400);
    }

    @Test
    @Order(4)
    @DisplayName("Fail Load All Users Has no Authorization")
    void failLoadAllUsersHasNoAuthorization() {
        HttpEntity<Object> entity = convertHttpEntity(null, "2");

        ResponseEntity<ExceptionResponse> responseEntity = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, entity, ExceptionResponse.class);
        ExceptionResponse response = responseEntity.getBody();

        assertThat(response.statusCode()).isEqualTo(403);
    }

    @Test
    @Order(5)
    @DisplayName("Success Login")
    void loginSuccess() {
        IdResponse idResponse = restTemplate.postForObject(baseUrl + "/user/login", new LoginRequest("user0@gmail.com", "Password0!"), IdResponse.class);

        assertThat(idResponse.id()).isNotZero();
    }

    @Test
    @Order(6)
    @DisplayName("Fail Login With Wrong Info")
    void loginFail() {
        ExceptionResponse exceptionResponse = restTemplate.postForObject(baseUrl + "/user/login", new LoginRequest("wrong", "wrong"), ExceptionResponse.class);

        assertThat(exceptionResponse.statusCode()).isEqualTo(400);
    }

    private List<SignUpResponse> bulkCreateUser() {
        List<SignUpResponse> responses = new ArrayList<>();
        String requestUrl = baseUrl + "/user";

        for (int i = 0; i < 10; i++) {
            SignUpResponse response = createUser(requestUrl, i);
            responses.add(response);
        }

        return responses;
    }

    private SignUpResponse createUser(String requestUrl, int indexNumber) {
        String email = "user" + indexNumber + "@gmail.com";
        String password = "Password" + indexNumber + "!";
        String name = "User" + indexNumber;
        SignUpRequest request = new SignUpRequest(email, password, name);

        return restTemplate
                .postForEntity(requestUrl, request, SignUpResponse.class)
                .getBody();
    }

}
