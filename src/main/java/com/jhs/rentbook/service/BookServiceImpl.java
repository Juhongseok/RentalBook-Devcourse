package com.jhs.rentbook.service;

import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.book.RentalStatus;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.global.exception.custom.EntityNotFoundException;
import com.jhs.rentbook.repository.BookRepository;
import com.jhs.rentbook.repository.UserRepository;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jhs.rentbook.domain.book.RentalStatus.RENT;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll((root, query, builder) -> null);
    }

    @Override
    public Book findBookInfo(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디를 가진 책을 찾을 수 없습니다"));
    }

    @Override
    public List<UserBookRental> findAllRentalInfo() {
        return bookRepository.findAll(
                        (root, query, builder) ->
                                builder.and(builder.equal(root.get("rental"), RENT))
                ).stream()
                .map(Book::rentals)
                .toList();
    }

    @Override
    public List<UserBookRental> findAllRentalInfoByUserId(Long userId) {
        return bookRepository.findAll(
                        (root, query, builder) -> {
                            Path<Long> target = root.get("rentalInfo").get("user").get("id");
                            return builder.and(builder.equal(target, userId));
                        }
                ).stream()
                .map(Book::rentals)
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
