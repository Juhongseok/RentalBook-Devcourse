package com.jhs.rentbook.domain.user;

import com.jhs.rentbook.global.exception.NotMatchException;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@Embeddable
@NoArgsConstructor
public class Account {

    private static final String EMAIL_PATTERN = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b";
    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    private String email;
    private String password;

    public Account(String email, String password) {
        validate(email, password);
        this.email = email;
        this.password = password;
    }

    private void validate(String email, String password) {
        validateEmail(email);
        validatePassword(password);
    }

    private void validateEmail(String email) {
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            throw new NotMatchException("이메일 형식을 맞춰서 작성해 주세요");
        }
    }

    private void validatePassword(String password) {
        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new NotMatchException("비밀번호 형식을 맞춰서 작성해 주세요");
        }
    }

    public boolean correctPassword(String password) {
        return this.password.equals(password);
    }
}
