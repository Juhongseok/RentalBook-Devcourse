package com.jhs.rentbook.controller;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.rental.RentalBookInfo;
import com.jhs.rentbook.controller.dto.user.*;
import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.domain.user.vo.UserVo;
import com.jhs.rentbook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<UserInfo> getAllUserList() {
        return userService.getUserList().stream()
                .map(User::values)
                .map(value -> new UserInfo(value.userId(), value.username(), value.email(), value.role()))
                .toList();
    }

    @PostMapping("/user/login")
    public IdResponse login(@RequestBody LoginRequest request) {
        UserVo value = userService.login(request.email(), request.password()).values();

        return new IdResponse(value.userId());
    }

    @PostMapping("/user")
    public SignUpResponse signUpUser(@RequestBody SignUpRequest request) {
        User user = User.of(request.email(), request.password(), request.name());
        UserVo value = userService.saveUser(user).values();

        return new SignUpResponse(value.userId(), value.email());
    }

    @GetMapping("/user/{userId}/rental")
    public List<RentalBookInfo> getOwnRentalBooks(@PathVariable Long userId) {
        return userService.getRentalBookList(userId).stream()
                .map(UserBookRental::values)
                .map(value -> new RentalBookInfo(value.bookValue().id(), value.bookValue().name(), value.bookValue().type()))
                .toList();
    }
}
