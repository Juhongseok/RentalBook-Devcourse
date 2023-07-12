package com.jhs.rentbook.controller;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.book.BookInfo;
import com.jhs.rentbook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @PostMapping("/book")
    public IdResponse saveBook() {
        return new IdResponse(0L);
    }

    @GetMapping("/books")
    public List<BookInfo> getAllBooks() {
        return new ArrayList<>();
    }


}
