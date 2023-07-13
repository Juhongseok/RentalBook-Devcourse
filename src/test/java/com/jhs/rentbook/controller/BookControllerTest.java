package com.jhs.rentbook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhs.rentbook.controller.dto.book.BookInfo;
import com.jhs.rentbook.controller.dto.book.SaveBookRequest;
import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.service.BookService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService service;

    @Test
    @DisplayName("책 등록 api")
    void saveBook() throws Exception {
        SaveBookRequest request = new SaveBookRequest("함께 자라기", "DEVELOP");
        Book book = new Book(null, "함께 자라기", DEVELOP, RETURNED);
        Book savedBook = new Book(1L, "함께 자라기", DEVELOP, RETURNED);
        given(service.save(book)).willReturn(savedBook);

        ResultActions action = mvc.perform(post("/book")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)));

        action.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("모든 등록된 책 정보 가져오기 api")
    void getAllBookInfo() throws Exception {
        List<Book> books = List.of(
                new Book(1L, "함께 자라기", DEVELOP, RETURNED),
                new Book(2L, "토비의 스프링", DEVELOP, RETURNED)
        );
        List<BookInfo> bookInfos = List.of(
                new BookInfo(1L, "함께 자라기", "DEVELOP", "RETURNED"),
                new BookInfo(2L, "토비의 스프링", "DEVELOP", "RETURNED")
        );
        given(service.findAll()).willReturn(books);

        ResultActions action = mvc.perform(get("/books"));

        action.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookInfos)))
                .andExpect(jsonPath("$.size()").value(books.size()));
    }


}
