package com.jhs.rentbook.repository;

import com.jhs.rentbook.domain.book.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @EntityGraph(attributePaths = {"rentalInfo.user"})
    List<Book> findAll(Specification<Book> spec);

}
