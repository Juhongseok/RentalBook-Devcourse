package com.jhs.rentbook.controller;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.book.BookInfo;
import com.jhs.rentbook.controller.dto.book.SaveBookRequest;
import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @ResponseStatus(CREATED)
    @PostMapping("/book")
    public IdResponse saveBook(@RequestBody @Validated SaveBookRequest request) {
        Book book = Book.of(request.bookName(), request.bookType());

        Book savedBook = service.save(book);

        return new IdResponse(savedBook.getId());
    }

    @GetMapping("/books")
    public List<BookInfo> getAllBooks() {
        return service.findAll().stream()
                .map(Book::values)
                .map(value -> new BookInfo(value.id(), value.name(), value.type(), value.rentalStatus()))
                .toList();
    }

}
