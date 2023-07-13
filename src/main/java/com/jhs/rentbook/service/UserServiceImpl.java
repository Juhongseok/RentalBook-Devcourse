package com.jhs.rentbook.service;

import com.jhs.rentbook.domain.rental.UserBookRental;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.global.exception.custom.BusinessException;
import com.jhs.rentbook.global.exception.custom.NotMatchException;
import com.jhs.rentbook.repository.UserBookRentalRepository;
import com.jhs.rentbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        checkDuplicateEmail(user);

        return userRepository.save(user);
    }

    private void checkDuplicateEmail(User user) {
        Optional<User> findUser = userRepository.findByAccountEmail(user.values().email());

        if (findUser.isPresent()) {
            throw new BusinessException("이미 사용중인 이메일입니다");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserBookRental> getRentalBookList(Long userId) {
        return userBookRentalRepository.findBooksByUserId(userId);
    }
}
