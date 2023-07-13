package com.jhs.rentbook.service;

import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.global.exception.NotMatchException;
import com.jhs.rentbook.repository.UserBookRentalRepository;
import com.jhs.rentbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserBookRentalRepository userBookRentalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByAccountEmail(email)
                .orElseThrow(() -> new NotMatchException("존재하지 않은 이메일입니다"));

        user.checkPassword(password);

        return user;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBookRental> getRentalBookList(Long userId) {
        return userBookRentalRepository.findBooksByUserId(userId);
    }
}
