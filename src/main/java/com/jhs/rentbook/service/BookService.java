package com.jhs.rentbook.service;

import com.jhs.rentbook.domain.book.Book;

import java.util.List;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();

    Book findBookInfo(Long bookId);

}
