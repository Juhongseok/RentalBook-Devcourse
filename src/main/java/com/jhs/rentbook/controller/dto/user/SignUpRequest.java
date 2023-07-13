package com.jhs.rentbook.controller.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record SignUpRequest(
        @NotEmpty
        String email,
        @NotEmpty
        String password,
        @NotEmpty
        String name) {
}
