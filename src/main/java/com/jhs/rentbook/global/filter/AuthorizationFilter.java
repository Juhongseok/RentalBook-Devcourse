package com.jhs.rentbook.global.filter;

import com.jhs.rentbook.global.exception.custom.filter.AuthorizationException;
import com.jhs.rentbook.global.filter.AuthenticationStorage.StorageField;
import com.jhs.rentbook.global.filter.matcher.AuthorizationManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter implements Filter {

    private final AuthenticationStorage storage;
    private final AuthorizationManager manager;

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StorageField field = storage.get();
        HttpServletRequest request = (HttpServletRequest) req;

        if (!manager.canAccess(request, field)) {
            throw new AuthorizationException("Has No Access Authorize");
        }

        chain.doFilter(req, response);
    }

}
