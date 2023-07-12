package com.jhs.rentbook.domain.book;

import com.jhs.rentbook.domain.BaseTimeEntity;
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
}
