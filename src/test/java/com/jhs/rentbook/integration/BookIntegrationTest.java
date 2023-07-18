package com.jhs.rentbook.integration;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.book.SaveBookRequest;
import com.jhs.rentbook.controller.dto.user.SignUpRequest;
import com.jhs.rentbook.controller.dto.user.SignUpResponse;
import com.jhs.rentbook.domain.book.BookType;
import com.jhs.rentbook.global.exception.ExceptionResponse;
import com.jhs.rentbook.integration.base.AbstractIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;

class BookIntegrationTest extends AbstractIntegrationTest {

    @Test
    @Disabled
    @Order(1)
    @DisplayName("create Books 10")
    void createBulkBooks() {
        List<IdResponse> idResponses = bulkCreateBook();

        assertThat(idResponses).hasSize(10);
    }

    @Test
    @Order(2)
    @DisplayName("fail create books has no authorization")
    void failCreateBook() {
        SignUpResponse userResponse = createDummyUser();
        HttpEntity<Object> entity = convertHttpEntity(null, String.valueOf(userResponse.userId()));

        ResponseEntity<ExceptionResponse> responseEntity = restTemplate.exchange(baseUrl + "/book", HttpMethod.GET, entity, ExceptionResponse.class);
        ExceptionResponse response = responseEntity.getBody();

        assertThat(response.statusCode()).isEqualTo(403);
    }

    private SignUpResponse createDummyUser() {
        SignUpRequest request = new SignUpRequest("dummy@gmail.com", "Password1!", "dummy");

        return restTemplate
                .postForEntity(baseUrl + "/user", request, SignUpResponse.class)
                .getBody();
    }

    private List<IdResponse> bulkCreateBook() {
        List<IdResponse> responses = new ArrayList<>();
        String requestUrl = baseUrl + "/book";

        for (int i = 0; i < 10; i++) {
            IdResponse response = createBook(requestUrl, i);
            responses.add(response);
        }

        return responses;
    }

    private IdResponse createBook(String requestUrl, int indexNumber) {
        String bookName = "book" + indexNumber;
        String bookType = indexNumber % 2 == 0 ? BookType.DEVELOP.name() : BookType.LANGUAGE.name();
        SaveBookRequest request = new SaveBookRequest(bookName, bookType);
        HttpEntity<SaveBookRequest> entity = convertHttpEntity(request, "1");

        return restTemplate
                .exchange(requestUrl, POST, entity, IdResponse.class)
                .getBody();
    }

}
