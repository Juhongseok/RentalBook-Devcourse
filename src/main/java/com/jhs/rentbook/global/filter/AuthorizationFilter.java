package com.jhs.rentbook.global.filter;

import com.jhs.rentbook.global.exception.custom.filter.AuthorizationException;
import com.jhs.rentbook.global.filter.AuthenticationStorage.StorageField;
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
    private static final String NO_ROLE = "NO_ROLE";

    private final Map<String, List<RequestMatcher>> accessUris = Map.of(
            "USER", List.of(new RequestMatcher(GET, "/books"), new RequestMatcher(DELETE, "/rental/*"), new RequestMatcher(POST, "/rental/book"),
                    new RequestMatcher(GET, "/user/*/rental")),
            "MANAGER", List.of(new RequestMatcher(POST, "/book"), new RequestMatcher(GET, "/rental/books"), new RequestMatcher(GET, "/users")
                    , new RequestMatcher(GET, "/books")),
            NO_ROLE, List.of(new RequestMatcher(POST, "/user/login"), new RequestMatcher(POST, "/user"))
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        StorageField field = storage.get();
        HttpServletRequest request = (HttpServletRequest) req;

        if (canNotAccess(field, request)) {
            throw new AuthorizationException("Has No Access Authorize");
        }

        chain.doFilter(req, response);
    }

    private boolean canNotAccess(StorageField field, HttpServletRequest request) {
        String role = field == null ? NO_ROLE : field.role();
        List<RequestMatcher> requestMatchers = accessRequestMatchers(role);

        boolean result = false;
        for (RequestMatcher requestMatcher : requestMatchers) {
            result = requestMatcher.isMatch(request, field);

            if (result) {
                break;
            }
        }

        return !result;
    }

    private List<RequestMatcher> accessRequestMatchers(String role) {
        List<RequestMatcher> totalAccessUris = new ArrayList<>();
        totalAccessUris.addAll(accessUris.get(role));
        if (!role.equals(NO_ROLE)) {
            totalAccessUris.addAll(accessUris.get(NO_ROLE));
        }

        return totalAccessUris;
    }
}
