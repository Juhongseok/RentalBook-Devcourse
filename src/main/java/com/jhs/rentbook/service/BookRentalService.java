package com.jhs.rentbook.service;

import com.jhs.rentbook.controller.dto.rental.ReturnBookIds;
import com.jhs.rentbook.domain.rental.UserBookRental;

import java.util.List;

public interface BookRentalService {

    List<UserBookRental> findAllRentalInfo();

    Long returnBook(Long rentalId, ReturnBookIds ids);

    UserBookRental rentBook(Long userId, Long bookId);
}
