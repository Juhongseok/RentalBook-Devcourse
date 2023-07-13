package com.jhs.rentbook.service;

import com.jhs.rentbook.controller.dto.rental.ReturnBookIds;
import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.book.BookType;
import com.jhs.rentbook.domain.book.RentalStatus;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.user.Account;
import com.jhs.rentbook.domain.user.Role;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.global.exception.EntityNotFoundException;
import com.jhs.rentbook.global.exception.NotMatchException;
import com.jhs.rentbook.repository.UserBookRentalRepository;
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

    @InjectMocks
    private BookRentalServiceImpl bookRentalService;

    @Test
    @DisplayName("책 반납에 성공한다")
    void successReturnBook() {
        UserBookRental rental = new UserBookRental(1L, new User(1L, "user", new Account("user@gmail.com", "Password1!"), Role.USER), new Book(1L, "함께 자라기", BookType.DEVELOP, RentalStatus.RETURNED));
        given(userBookRentalRepository.findById(1L)).willReturn(Optional.of(rental));

        Long returnBookId = bookRentalService.returnBook(1L, new ReturnBookIds(1L, 1L));

        assertThat(returnBookId).isEqualTo(1L);
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

}
