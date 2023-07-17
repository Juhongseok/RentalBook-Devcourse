package com.jhs.rentbook.repository;

import com.jhs.rentbook.domain.book.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"rentalInfo", "rentalInfo.user"})
    List<Book> findAll();

}
