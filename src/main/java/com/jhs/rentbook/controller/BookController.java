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
    public RentalBookInfo rentBook(@PathVariable Long bookId, @RequestParam Long userId) {
        Book rentalBook = service.rentBook(userId, bookId);
        BookVo bookVo = rentalBook.values();

        return new RentalBookInfo(bookVo.id(), bookVo.name(), bookVo.type());
    }

    @DeleteMapping("/book/{bookId}/rental/user/{userId}")
    public IdResponse returnRentalBook(@PathVariable Long bookId, @RequestParam Long rentalId) {
        return new IdResponse(service.returnBook(rentalId, bookId));
    }

    @GetMapping("/book/rental/user/{userId}")
    public List<RentalBookInfo> getOwnRentalBooks(@PathVariable Long userId) {
        return service.findAllRentalInfoByUserId(userId).stream()
                .map(UserBookRental::getBookValue)
                .map(value -> new RentalBookInfo(value.id(), value.name(), value.type()))
                .toList();
    }

    @GetMapping("/book/rental/users")
    public List<RentalBook> getAllRentalBooks() {
        List<UserBookRental> rentals = service.findAllRentalInfo();
        Map<Long, List<RentalBookInfo>> map = rentalBookGroupByUserId(rentals);

        return rentals.stream()
                .map(UserBookRental::values)
                .map(value -> new RentalBook(value.userValue().userId(), value.userValue().username(), map.get(value.userValue().userId())))
                .toList();
    }

    private Map<Long, List<RentalBookInfo>> rentalBookGroupByUserId(List<UserBookRental> rentals) {
        Map<Long, List<RentalBookInfo>> map = new HashMap<>();

        for (UserBookRental rental : rentals) {
            UserBookRentalVo value = rental.values();

            Long userId = value.userValue().userId();
            List<RentalBookInfo> list = map.getOrDefault(userId, new ArrayList<>());
            list.add(new RentalBookInfo(value.bookValue().id(), value.bookValue().name(), value.bookValue().type()));
            map.put(userId, list);
        }

        return map;
    }

}
