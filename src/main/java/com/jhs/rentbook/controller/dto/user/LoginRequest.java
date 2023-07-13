package com.jhs.rentbook.controller.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty
        String email,
        @NotEmpty
        String password
) {
}
