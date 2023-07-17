package com.jhs.rentbook.service;

import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.global.exception.custom.EntityNotFoundException;
import com.jhs.rentbook.repository.BookRepository;
import com.jhs.rentbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findBookInfo(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 책을 찾을 수 없습니다"));
    }

    @Override
    public List<UserBookRental> findAllRentalInfo() {
        return bookRepository.findAll().stream()
                .map(Book::rentals)
                .toList();
    }

    @Override
    public List<UserBookRental> findAllRentalInfoByUserId(Long userId) {
        return bookRepository.findAll().stream()
                .map(Book::rentals)
                .filter(userBookRental -> userBookRental.checkUserId(userId))
                .toList();
    }

    @Override
    @Transactional
    public Book rentBook(Long userId, Long bookId) {
        User user = userRepository.getReferenceById(userId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("해당 책을 찾을 수 없습니다"));

        return book.rent(user);
    }

    @Override
    @Transactional
    public Long returnBook(Long rentalId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("해당 책을 찾을 수 없습니다"));
        book.returnBook(rentalId);

        return rentalId;
    }
}
