package com.jhs.rentbook.service;

import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.book.BookType;
import com.jhs.rentbook.domain.book.RentalStatus;
import com.jhs.rentbook.global.exception.custom.EntityNotFoundException;
import com.jhs.rentbook.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("아이디를 기준으로 책을 찾는데 성공한다")
    void successFindBook() {
        Book book = new Book(1L, "함께 자라기", BookType.DEVELOP, RentalStatus.RETURNED);
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        Book findBook = bookService.findBookInfo(1L);

        assertThat(findBook.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("아이디를 기준으로 책을 찾는데 실패한다")
    void failFindBook() {
        Book book = new Book(1L, "함께 자라기", BookType.DEVELOP, RentalStatus.RETURNED);
        given(bookRepository.findById(2L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findBookInfo(2L))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("해당 아이디를 가진 책을 찾을 수 없습니다");
    }

}
