package com.jhs.rentbook.service;

import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.rental.UserBookRental;

import java.util.List;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();

    Book findBookInfo(Long bookId);

    List<UserBookRental> findAllRentalInfo();

    List<UserBookRental> findAllRentalInfoByUserId(Long userId);

    Book rentBook(Long userId, Long bookId);

    Long returnBook(Long rentalId, Long bookId);

}
