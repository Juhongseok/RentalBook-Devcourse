package com.jhs.rentbook.service;

import com.jhs.rentbook.controller.dto.rental.ReturnBookIds;
import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.book.BookType;
import com.jhs.rentbook.domain.book.RentalStatus;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.rental.vo.UserBookRentalVo;
import com.jhs.rentbook.domain.user.Account;
import com.jhs.rentbook.domain.user.Role;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.global.exception.custom.BusinessException;
import com.jhs.rentbook.global.exception.custom.EntityNotFoundException;
import com.jhs.rentbook.global.exception.custom.NotMatchException;
import com.jhs.rentbook.repository.BookRepository;
import com.jhs.rentbook.repository.UserBookRentalRepository;
import com.jhs.rentbook.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookRentalServiceImplTest {

    @Mock
    private UserBookRentalRepository userBookRentalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookRentalServiceImpl bookRentalService;

    @Test
    @DisplayName("책 반납에 성공한다")
    void successReturnBook() {
        User user = new User(1L, "user", new Account("user@gmail.com", "Password1!"), Role.USER);
        Book book = new Book(1L, "함께 자라기", BookType.DEVELOP, RentalStatus.RETURNED);
        UserBookRental rental = new UserBookRental(1L, user, book);
        given(userBookRentalRepository.findById(1L)).willReturn(Optional.of(rental));

        Long returnBookId = bookRentalService.returnBook(1L, new ReturnBookIds(1L, 1L));

        assertThat(returnBookId).isEqualTo(1L);
        assertThat(book.isRenting()).isFalse();
    }

    @Test
    @DisplayName("대여 정보 오류로 인해 책 반납에 실패한다")
    void failReturnBook() {
        UserBookRental rental = new UserBookRental(1L, new User(1L, "user", new Account("user@gmail.com", "Password1!"), Role.USER), new Book(1L, "함께 자라기", BookType.DEVELOP, RentalStatus.RETURNED));
        given(userBookRentalRepository.findById(1L)).willReturn(Optional.of(rental));

        ReturnBookIds ids = new ReturnBookIds(1L, 2L);

        assertThatThrownBy(() -> bookRentalService.returnBook(1L, ids))
                .isExactlyInstanceOf(NotMatchException.class)
                .hasMessage("지우고자 하는 이력의 내용가 다른 아이디입니다");
    }
    
    @Test
    @DisplayName("대여 번호 오류로 인해 책 반납에 실패한다")
    void failReturnBookWithWrongRentalId() {
        given(userBookRentalRepository.findById(100L)).willReturn(Optional.empty());

        ReturnBookIds ids = new ReturnBookIds(1L, 2L);

        assertThatThrownBy(() -> bookRentalService.returnBook(100L, ids))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 대여 이력을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("책 대여에 성공한다")
    void successRentBook() {
        User user = new User(1L, "user", new Account("user@gmail.com", "Password1!"), Role.USER);
        Book book = new Book(1L, "함께 자라기", BookType.DEVELOP, RentalStatus.RETURNED);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(userBookRentalRepository.save(new UserBookRental(user, book))).willReturn(new UserBookRental(1L, user, book));

        UserBookRental rentedBook = bookRentalService.rentBook(1L, 1L);
        UserBookRentalVo values = rentedBook.values();

        assertThat(values.userValue().userId()).isEqualTo(1L);
        assertThat(values.bookValue().id()).isEqualTo(1L);
        assertThat(book.isRenting()).isTrue();
    }

    @Test
    @DisplayName("사용자 검색에 실패하여 책 대여에 실패한다")
    void failRentBookWithWrongUserId() {
        given(userRepository.findById(2L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> bookRentalService.rentBook(2L, 1L))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("책 검색에 실패하여 책 대여에 실패한다")
    void failRentBookWithWrongBookId() {
        User user = new User(1L, "user", new Account("user@gmail.com", "Password1!"), Role.USER);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(bookRepository.findById(2L)).willReturn(Optional.empty());

       assertThatThrownBy(() -> bookRentalService.rentBook(1L, 2L))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 책을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("이미 대여중인 책이기 때문에 책 대여에 실패한다")
    void failRentBookWithAlreadyRentedBook() {
        User user = new User(1L, "user", new Account("user@gmail.com", "Password1!"), Role.USER);
        Book book = new Book(1L, "함께 자라기", BookType.DEVELOP, RentalStatus.RENT);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));
        given(userBookRentalRepository.save(new UserBookRental(user, book))).willReturn(new UserBookRental(1L, user, book));

        assertThatThrownBy(() -> bookRentalService.rentBook(1L, 1L))
                .isExactlyInstanceOf(BusinessException.class)
                .hasMessage("이미 대여중인 도서입니다");
        assertThat(book.isRenting()).isTrue();
    }

}
