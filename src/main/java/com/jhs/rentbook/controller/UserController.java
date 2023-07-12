package com.jhs.rentbook.controller;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.rental.RentalBookInfo;
import com.jhs.rentbook.controller.dto.user.*;
import com.jhs.rentbook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<UserInfo> getAllUserList() {
        return new ArrayList<>();
    }

    @PostMapping("/user/login")
    public IdResponse login(@RequestBody LoginRequest request) {
        return new IdResponse(0L);
    }

    @PostMapping("/user")
    public SignUpResponse signUpUser(@RequestBody SignUpRequest request) {
        return new SignUpResponse(0L, "");
    }

    @GetMapping("/user/{userId}/rental")
    public List<RentalBookInfo> getOwnRentalBooks(@PathVariable Long userId) {
        return new ArrayList<>();
    }
}
