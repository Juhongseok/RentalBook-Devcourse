package com.jhs.rentbook.controller;

import com.jhs.rentbook.controller.dto.IdResponse;
import com.jhs.rentbook.controller.dto.user.LoginRequest;
import com.jhs.rentbook.controller.dto.user.SignUpRequest;
import com.jhs.rentbook.controller.dto.user.SignUpResponse;
import com.jhs.rentbook.controller.dto.user.UserInfo;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.domain.user.vo.UserVo;
import com.jhs.rentbook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

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

    @ResponseStatus(CREATED)
    @PostMapping("/user/login")
    public IdResponse login(@RequestBody @Validated LoginRequest request) {
        User loginUser = userService.login(request.email(), request.password());

        return new IdResponse(loginUser.getId());
    }

    @ResponseStatus(CREATED)
    @PostMapping("/user")
    public SignUpResponse signUpUser(@RequestBody @Validated SignUpRequest request) {
        User user = User.of(request.email(), request.password(), request.name());
        UserVo value = userService.saveUser(user).values();

        return new SignUpResponse(value.userId(), value.email());
    }

}
