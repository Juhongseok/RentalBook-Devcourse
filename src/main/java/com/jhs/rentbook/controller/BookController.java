package com.jhs.rentbook.controller;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.book.BookInfo;
import com.jhs.rentbook.controller.dto.book.SaveBookRequest;
import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.book.BookType;
import com.jhs.rentbook.domain.book.vo.BookVo;
import com.jhs.rentbook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @PostMapping("/book")
    public IdResponse saveBook(@RequestBody SaveBookRequest request) {
        Book book = Book.of(request.bookName(), request.bookType());

        BookVo values = service.save(book).values();

        return new IdResponse(values.id());
    }

    @GetMapping("/books")
    public List<BookInfo> getAllBooks() {
        return service.findAll().stream()
                .map(Book::values)
                .map(value -> new BookInfo(value.id(), value.name(), value.type(), value.rentalStatus()))
                .toList();
    }


}
