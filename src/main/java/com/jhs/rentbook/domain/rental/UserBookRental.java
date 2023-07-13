package com.jhs.rentbook.domain.rental;

import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.rental.vo.UserBookRentalVo;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.global.exception.custom.NotMatchException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@Table(name = "USER_BOOKS_RENTAL")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserBookRental {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = {REMOVE})
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    public UserBookRentalVo values() {
        return new UserBookRentalVo(id, user.values(), book.values());
    }

    public void checkRentalInfo(Long userId, Long bookId) {
        Long savedUserId = user.getId();
        Long savedBookId = book.getId();

        if (!userId.equals(savedUserId) || !bookId.equals(savedBookId)) {
            throw new NotMatchException("지우고자 하는 이력의 내용가 다른 아이디입니다");
        }
    }
}
