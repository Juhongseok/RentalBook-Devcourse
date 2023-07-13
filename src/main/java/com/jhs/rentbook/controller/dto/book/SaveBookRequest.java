package com.jhs.rentbook.controller.dto.book;

import jakarta.validation.constraints.NotEmpty;

public record SaveBookRequest(
        @NotEmpty
        String bookName,
        @NotEmpty
        String bookType) {
}
