package com.jhs.rentbook.domain.rental;

import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.rental.vo.UserBookRentalVo;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.global.exception.custom.NotMatchException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "USER_BOOKS_RENTAL")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserBookRental {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    public UserBookRental(User user, Book book) {
        this.user = user;
        this.book = book;
    }

    public UserBookRentalVo values() {
        return new UserBookRentalVo(id, user.values(), book.values());
    }

    public void checkId(Long rentalId) {
        if (!this.id.equals(rentalId)) {
            throw new NotMatchException("아이디가 일치하지 않습니다");
        }
    }
}
