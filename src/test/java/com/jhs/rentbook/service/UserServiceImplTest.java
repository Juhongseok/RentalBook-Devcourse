package com.jhs.rentbook.service;

import com.jhs.rentbook.domain.user.Account;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.global.exception.custom.NotMatchException;
import com.jhs.rentbook.repository.UserBookRentalRepository;
import com.jhs.rentbook.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.jhs.rentbook.domain.user.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserBookRentalRepository userBookRentalRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("로그인에 성공한다")
    void successLogin() {
        User user = new User(1L, "user1", new Account("user1@gmail.com", "Password1!"), USER);
        given(userRepository.findByAccountEmail("user1@gmail.com")).willReturn(Optional.of(user));

        User loginUser = userService.login("user1@gmail.com", "Password1!");

        assertThat(loginUser).isEqualTo(user);
        assertThat(loginUser.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("이메일이 일치하지 않아 로그인에 실패한다")
    void failLoginWithWrongEmail() {
        given(userRepository.findByAccountEmail("wrongEmail")).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login("wrongEmail", "password"))
                .isExactlyInstanceOf(NotMatchException.class)
                .hasMessage("존재하지 않은 이메일입니다");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않아 로그인에 실패한다")
    void failLoginWithWrongPassword() {
        User user = new User(1L, "user1", new Account("user1@gmail.com", "Password1!"), USER);
        given(userRepository.findByAccountEmail("user1@gmail.com")).willReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.login("user1@gmail.com", "Password!"))
                .isExactlyInstanceOf(NotMatchException.class)
                .hasMessage("비밀번호가 일치하지 않습니다");
    }

}
