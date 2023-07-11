package com.jhs.rentbook.repository;

import com.jhs.rentbook.domain.UserBookRental;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBookRentalRepository extends JpaRepository<UserBookRental, Long> {

    @EntityGraph(attributePaths = {"user", "book"})
    List<UserBookRental> findAllInfo();

    @EntityGraph(attributePaths = {"book"})
    List<UserBookRental> findBooksByUserId(Long userId);

    @EntityGraph(attributePaths = {"book"})
    List<UserBookRental> findAllBooks();

}
