package com.jhs.rentbook.domain.user;

import com.jhs.rentbook.domain.BaseTimeEntity;
import com.jhs.rentbook.domain.user.vo.UserVo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Embedded
    private Account account;

    @Enumerated(STRING)
    private Role role;

    public static User of(String email, String password, String name) {
        return new User(null, name, new Account(email, password), Role.USER);
    }

    public UserVo values() {
        return new UserVo(id, name, account.getEmail(), role.name());
    }
}
