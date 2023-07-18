package com.jhs.rentbook.repository;

import com.jhs.rentbook.domain.book.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b join fetch b.rentalInfo ri join fetch ri.user u where u.id = :userId")
    List<Book> findAllByUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"rentalInfo", "rentalInfo.user"})
    List<Book> findAll();

}
