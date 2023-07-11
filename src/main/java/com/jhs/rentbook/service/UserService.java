package com.jhs.rentbook.service;

import com.jhs.rentbook.domain.User;
import com.jhs.rentbook.domain.UserBookRental;

import java.util.List;

public interface UserService {

    List<User> getUserList();

    User login(String email, String password);

    User saveUser(User user);

    List<UserBookRental> getRentalBookList(Long userId);

}
