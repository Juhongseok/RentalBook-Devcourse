package com.jhs.rentbook.service;

import com.jhs.rentbook.controller.dto.rental.ReturnBookIds;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.global.exception.custom.EntityNotFoundException;
import com.jhs.rentbook.repository.UserBookRentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookRentalServiceImpl implements BookRentalService{

    private final UserBookRentalRepository userBookRentalRepository;

    @Override
    public List<UserBookRental> findAllRentalInfo() {
        return userBookRentalRepository.findAll();
    }

    @Override
    public Long returnBook(Long rentalId, ReturnBookIds ids) {
        UserBookRental userBookRental = userBookRentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 대여 이력을 찾을 수 없습니다"));

        userBookRental.checkRentalInfo(ids.userId(), ids.bookId());
        userBookRentalRepository.delete(userBookRental);

        return rentalId;
    }
}
