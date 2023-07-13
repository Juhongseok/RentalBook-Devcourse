package com.jhs.rentbook.global;

import com.jhs.rentbook.domain.user.Account;
import com.jhs.rentbook.domain.user.Role;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostProcessor implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        userRepository.save(new User(null, "manager", new Account("manager@gmail.com", "Manager1!"), Role.MANAGER));
    }
}
