package com.jhs.rentbook.domain.book;

import com.jhs.rentbook.domain.BaseTimeEntity;
import com.jhs.rentbook.domain.book.vo.BookVo;
import com.jhs.rentbook.global.exception.custom.NotMatchException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Arrays;

import static jakarta.persistence.EnumType.STRING;
import static java.util.stream.Collectors.joining;

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
        BookType bookType = retrieveBookType(type);

        return new Book(null, name, bookType, RentalStatus.RETURNED);
    }

    private static BookType retrieveBookType(String type) {
        try {
            return BookType.valueOf(type);
        } catch (IllegalArgumentException exception) {
            String typeList = Arrays.stream(BookType.values())
                    .map(BookType::name)
                    .collect(joining(", "));

            String message = "일치하는 책의 타입이 없습니다. 다음 타입 중 하나를 골라주세요 (" + typeList + ")";

            throw new NotMatchException(message);
        }
    }

    public Long getId() {
        return id;
    }

    public void rent() {
        this.rental = RentalStatus.RENT;
    }

    public void returnBook() {
        this.rental = RentalStatus.RETURNED;
    }

    public boolean isRenting() {
        return this.rental.equals(RentalStatus.RENT);
    }
}
