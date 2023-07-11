package com.jhs.rentbook.domain;

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
}
