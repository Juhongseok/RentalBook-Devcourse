package com.jhs.rentbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.rental.RentalBookInfo;
import com.jhs.rentbook.controller.dto.user.LoginRequest;
import com.jhs.rentbook.controller.dto.user.SignUpRequest;
import com.jhs.rentbook.controller.dto.user.SignUpResponse;
import com.jhs.rentbook.controller.dto.user.UserInfo;
import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.book.BookType;
import com.jhs.rentbook.domain.book.RentalStatus;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.user.Account;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService service;

    @Test
    @DisplayName("모든 사용자 정보 불러오기 api")
    void getAllUserInfos() throws Exception {
        List<User> users = List.of(
                new User(1L, "user1", new Account("user1@gmail.com", "Password1!"), USER),
                new User(1L, "user2", new Account("user2@gmail.com", "Password2!"), USER)
        );
        List<UserInfo> userInfos = List.of(
                new UserInfo(1L, "user1", "user1@gmail.com", "USER"),
                new UserInfo(1L, "user2", "user2@gmail.com", "USER")
        );
        given(service.getUserList()).willReturn(users);

        ResultActions action = mvc.perform(get("/users"));

        action.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userInfos)));
    }

    @Test
    @DisplayName("사용자 로그인 api")
    void loginUser() throws Exception {
        LoginRequest request = new LoginRequest("user1@gamil.com", "Password1!");
        User user = new User(1L, "user1", new Account("user1@gmail.com", "Password1!"), USER);
        given(service.login(request.email(), request.password())).willReturn(user);

        ResultActions action = mvc.perform(post("/user/login")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request)));

        action.andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(new IdResponse(1L))));
    }

    @Test
    @DisplayName("사용자 생성 api")
    void signUpUser() throws Exception {
        SignUpRequest request = new SignUpRequest("user1@gmail.com", "Password1!", "user");
        User user = User.of(request.email(), request.password(), request.name());
        User savedUser = new User(1L, request.name(), new Account(request.email(), request.password()), USER);
        given(service.saveUser(user)).willReturn(savedUser);

        ResultActions action = mvc.perform(post("/user")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(request)));

        action.andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(new SignUpResponse(1L, "user1@gmail.com"))));
    }

    @Test
    @DisplayName("사용자 대여 책 목록 검색 api")
    void getUserRentalBooks() throws Exception {
        List<UserBookRental> userBookRentals = List.of(
                new UserBookRental(1L,
                        new User(1L, "user1", new Account("user1@gmail.com", "Password1!"), USER),
                        new Book(1L, "함께 자라기", DEVELOP, RETURNED)
                )
        );
        List<RentalBookInfo> response = List.of(
                new RentalBookInfo(1L, "함께 자라기", "DEVELOP")
        );
        given(service.getRentalBookList(1L)).willReturn(userBookRentals);

        ResultActions action = mvc.perform(get("/user/{userId}/rental", 1L));

        action.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(response)));
    }

}
