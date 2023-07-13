package com.jhs.rentbook.controller.dto.rental;

import jakarta.validation.constraints.NotNull;

public record RentalBookRequest(
        @NotNull
        Long userId,
        @NotNull
        Long bookId) {
}
