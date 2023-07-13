package com.jhs.rentbook.controller.dto.rental;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record ReturnBookIds(
        @NotNull @Range
        Long userId,
        @NotNull @Range
        Long bookId) {
}
