package com.jhs.rentbook.domain.book;

import com.jhs.rentbook.domain.BaseTimeEntity;
import com.jhs.rentbook.domain.book.vo.BookVo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "BOOKS")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Enumerated(STRING)
    private BookType type;

    @Enumerated(STRING)
    private RentalStatus rental;

    public BookVo values() {
        return new BookVo(id, name, type.name(), rental.name());
    }

    public static Book of(String name, String type) {
        return new Book(null, name, BookType.valueOf(type), RentalStatus.RETURNED);
    }
}
