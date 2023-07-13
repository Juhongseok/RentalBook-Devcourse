package com.jhs.rentbook.repository;

import com.jhs.rentbook.domain.rental.UserBookRental;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBookRentalRepository extends JpaRepository<UserBookRental, Long> {

    @EntityGraph(attributePaths = {"user", "book"})
    List<UserBookRental> findAll();

    @EntityGraph(attributePaths = {"book"})
    List<UserBookRental> findBooksByUserId(Long userId);

}
