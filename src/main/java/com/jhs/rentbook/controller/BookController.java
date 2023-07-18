package com.jhs.rentbook.controller;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.book.BookInfo;
import com.jhs.rentbook.controller.dto.book.SaveBookRequest;
import com.jhs.rentbook.controller.dto.rental.RentalBook;
import com.jhs.rentbook.controller.dto.rental.RentalBookInfo;
import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.book.vo.BookVo;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.rental.vo.UserBookRentalVo;
import com.jhs.rentbook.domain.user.vo.UserVo;
import com.jhs.rentbook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/book/{bookId}/rental")
    public IdResponse rentBook(@PathVariable Long bookId, @RequestParam Long userId) {
        Book rentalBook = service.rentBook(userId, bookId);
        BookVo bookVo = rentalBook.values();

        return new IdResponse(bookVo.id());
    }

    @DeleteMapping("/book/{bookId}/rental/user/{userId}")
    public IdResponse returnRentalBook(@PathVariable Long bookId, @RequestParam Long rentalId) {
        return new IdResponse(service.returnBook(rentalId, bookId));
    }

    @GetMapping("/book/rental/user/{userId}")
    public List<RentalBookInfo> getOwnRentalBooks(@PathVariable Long userId) {
        return service.findAllRentalInfoByUserId(userId).stream()
                .map(UserBookRental::values)
                .map(value -> new RentalBookInfo(value.rentalId(), value.bookValue().id(), value.bookValue().name(), value.bookValue().type()))
                .toList();
    }

    @GetMapping("/book/rental/users")
    public List<RentalBook> getAllRentalBooks() {
        List<UserBookRentalVo> values = service.findAllRentalInfo().stream()
                .map(UserBookRental::values)
                .toList();

        return convertToRentalBooks(values);
    }

    private List<RentalBook> convertToRentalBooks(List<UserBookRentalVo> values) {
        Map<Long, RentalBook> rentalBookMap = new HashMap<>();

        for (UserBookRentalVo value : values) {
            UserVo userVo = value.userValue();
            BookVo bookVo = value.bookValue();

            RentalBook rentalBook = rentalBookMap.getOrDefault(userVo.userId(), new RentalBook(userVo.userId(), userVo.username(), new ArrayList<>()));

            RentalBookInfo rentalBookInfo = new RentalBookInfo(value.rentalId(), bookVo.id(), bookVo.name(), bookVo.type());
            rentalBook.rentalList().add(rentalBookInfo);

            rentalBookMap.put(userVo.userId(), rentalBook);
        }

        return new ArrayList<>(rentalBookMap.values());
    }
}
