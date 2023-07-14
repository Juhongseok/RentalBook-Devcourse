package com.jhs.rentbook.global.filter.matcher;

import com.jhs.rentbook.global.filter.AuthenticationStorage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.POST;

@Component
public class RequestMatcherRegistry {

    private static final String NO_ROLE = "NO_ROLE";

    private final Map<String, List<RequestMatcher>> accessUris = Map.of(
            "USER", List.of(new RequestMatcher(GET, "/books"), new RequestMatcher(DELETE, "/rental/*"), new RequestMatcher(POST, "/rental/book"),
                    new UserIdDynamicRequestMatcher(GET, "/user/*/rental")),
            "MANAGER", List.of(new RequestMatcher(POST, "/book"), new RequestMatcher(GET, "/rental/books"), new RequestMatcher(GET, "/users")
                    , new RequestMatcher(GET, "/books")),
            NO_ROLE, List.of(new RequestMatcher(POST, "/user/login"), new RequestMatcher(POST, "/user"))
    );

    public boolean canNotAccess(AuthenticationStorage.StorageField field, HttpServletRequest request) {
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
