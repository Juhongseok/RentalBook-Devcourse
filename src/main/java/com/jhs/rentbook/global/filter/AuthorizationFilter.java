package com.jhs.rentbook.global.filter;

import com.jhs.rentbook.global.exception.custom.filter.AuthorizationException;
import com.jhs.rentbook.global.filter.AuthenticationStorage.StorageField;
import com.jhs.rentbook.global.filter.matcher.RequestMatcherRegistry;
import com.jhs.rentbook.global.filter.matcher.UserIdDynamicRequestMatcher;
import com.jhs.rentbook.global.filter.matcher.RequestMatcher;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter implements Filter {

    private final AuthenticationStorage storage;
    private final RequestMatcherRegistry registry;

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StorageField field = storage.get();
        HttpServletRequest request = (HttpServletRequest) req;

        if (registry.canNotAccess(field, request)) {
            throw new AuthorizationException("Has No Access Authorize");
        }

        chain.doFilter(req, response);
    }


}
