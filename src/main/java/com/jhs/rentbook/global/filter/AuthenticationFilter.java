package com.jhs.rentbook.global.filter;

import com.jhs.rentbook.domain.user.vo.UserVo;
import com.jhs.rentbook.global.exception.custom.EntityNotFoundException;
import com.jhs.rentbook.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

    private final AuthenticationStorage storage;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        String loginUserId = request.getHeader("LOGIN-ID");

        if (loginUserId != null) {
            UserVo values = userRepository.findById(Long.parseLong(loginUserId))
                    .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 사용자 접근입니다"))
                    .values();

            String role = values.role();
            storage.put(role);
        }

        chain.doFilter(request, response);
    }
}
