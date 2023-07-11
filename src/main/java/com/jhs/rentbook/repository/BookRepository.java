package com.jhs.rentbook.repository;

import com.jhs.rentbook.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
