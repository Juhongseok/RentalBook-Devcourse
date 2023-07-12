package com.jhs.rentbook.domain.rental;

import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.rental.vo.UserBookRentalVo;
import com.jhs.rentbook.domain.user.User;
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
}
