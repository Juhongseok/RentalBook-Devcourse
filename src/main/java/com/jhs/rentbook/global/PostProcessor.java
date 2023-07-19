package com.jhs.rentbook.global;

import com.jhs.rentbook.domain.book.Book;
import com.jhs.rentbook.domain.user.Account;
import com.jhs.rentbook.domain.user.User;
import com.jhs.rentbook.service.BookService;
import com.jhs.rentbook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static com.jhs.rentbook.domain.book.BookType.*;
import static com.jhs.rentbook.domain.book.RentalStatus.RETURNED;
import static com.jhs.rentbook.domain.user.Role.*;
import static com.jhs.rentbook.domain.user.Role.USER;

@Component
@RequiredArgsConstructor
public class PostProcessor implements CommandLineRunner {

    private final UserService userService;
    private final BookService bookService;

    @Override
    public void run(String... args) {
        userService.saveUser(new User(null, "manager", new Account("manager@gmail.com", "Manager1!"), MANAGER));
        userService.saveUser(new User(null, "user1", new Account("user1@gmail.com", "Password1!"), USER));
        userService.saveUser(new User(null, "user2", new Account("user2@gmail.com", "Password2!"), USER));
        userService.saveUser(new User(null, "user3", new Account("user3@gmail.com", "Password3!"), USER));
        userService.saveUser(new User(null, "user4", new Account("user4@gmail.com", "Password4!"), USER));
        userService.saveUser(new User(null, "user5", new Account("user5@gmail.com", "Password5!"), USER));

        bookService.save(new Book(null, "함께자라기", DEVELOP, RETURNED));
        bookService.save(new Book(null, "토비의 스프링 3.1", DEVELOP, RETURNED));
        bookService.save(new Book(null, "이펙티브 자바", DEVELOP, RETURNED));
        bookService.save(new Book(null, "도메인 주도 개발 시작하기", DEVELOP, RETURNED));
        bookService.save(new Book(null, "TOEIC", LANGUAGE, RETURNED));
        bookService.save(new Book(null, "정보처리기사", DEVELOP, RETURNED));
        bookService.save(new Book(null, "이것이 자바다", DEVELOP, RETURNED));
        bookService.save(new Book(null, "데이터베이스 시스템", DEVELOP, RETURNED));
        bookService.save(new Book(null, "운영체제", DEVELOP, RETURNED));
        bookService.save(new Book(null, "개념원리 기하와 벡터", ETC, RETURNED));

        bookService.rentBook(2L, 1L);
        bookService.rentBook(2L, 2L);
        bookService.rentBook(2L, 3L);
        bookService.rentBook(3L, 5L);
        bookService.rentBook(4L, 6L);
        bookService.rentBook(4L, 7L);
    }
}
