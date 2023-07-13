package com.jhs.rentbook.service;

import com.jhs.rentbook.controller.dto.rental.ReturnBookIds;
import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.global.exception.custom.EntityNotFoundException;
import com.jhs.rentbook.repository.BookRepository;
import com.jhs.rentbook.repository.UserBookRentalRepository;
import com.jhs.rentbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookRentalServiceImpl implements BookRentalService{

    private final UserBookRentalRepository userBookRentalRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public List<UserBookRental> findAllRentalInfo() {
        return userBookRentalRepository.findAll();
    }

    @Override
    public Long returnBook(Long rentalId, ReturnBookIds ids) {
        UserBookRental userBookRental = userBookRentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 대여 이력을 찾을 수 없습니다"));

        userBookRental.checkRentalInfo(ids.userId(), ids.bookId());
        userBookRental.returnBook();
        userBookRentalRepository.delete(userBookRental);

        return rentalId;
    }

    @Override
    public UserBookRental rentBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("해당 책을 찾을 수 없습니다"));

        UserBookRental userBookRental = userBookRentalRepository.save(new UserBookRental(user, book));
        userBookRental.rentBook();

        return userBookRental;
    }
}
