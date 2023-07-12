package com.jhs.rentbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.rental.RentalBook;
import com.jhs.rentbook.controller.dto.rental.RentalBookInfo;
import com.jhs.rentbook.controller.dto.rental.ReturnBookIds;
import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.user.Account;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.service.BookRentalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.jhs.rentbook.domain.book.BookType.DEVELOP;
import static com.jhs.rentbook.domain.book.RentalStatus.RETURNED;
import static com.jhs.rentbook.domain.user.Role.USER;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserBookRentalController.class)
class UserBookRentalControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    BookRentalService service;

    @Test
    @DisplayName("모든 대여 책 목록 api")
    void getAllRentalBooks() throws Exception {
        List<UserBookRental> userBookRentals = List.of(
                new UserBookRental(1L,
                        new User(1L, "user1", new Account("user1@gmail.com", "Password1!"), USER),
                        new Book(1L, "함께 자라기", DEVELOP, RETURNED)
                )
        );
        List<RentalBook> response = List.of(
                new RentalBook(1L, "user1", List.of(new RentalBookInfo(1L, "함께 자라기", "DEVELOP")))
        );
        given(service.findAllRentalInfo()).willReturn(userBookRentals);

        ResultActions action = mvc.perform(get("/rental/books"));

        action.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }


    @Test
    @DisplayName("책 반납 api")
    void returnRentalBook() throws Exception {
        ReturnBookIds ids = new ReturnBookIds(2L, 3L);
        given(service.returnBook(1L, ids)).willReturn(1L);

        ResultActions action = mvc.perform(delete("/rental/{rentalId}", 1L)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(ids)));

        action.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(new IdResponse(1L))));
    }

}
