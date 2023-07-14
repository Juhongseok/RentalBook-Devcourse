package com.jhs.rentbook.global.filter.matcher.request;

import com.jhs.rentbook.global.filter.matcher.AuthorizationManager.AuthorizationEntry;
import com.jhs.rentbook.global.filter.matcher.authorization.AuthorizationMatcher;
import com.jhs.rentbook.global.filter.matcher.authorization.RoleAuthorizationMatcher;
import com.jhs.rentbook.global.filter.matcher.authorization.UserIdAuthorizationMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;

@Component
public class RequestMatcherRegistry {

    private final Map<String, List<RequestMatcher>> accessUris = Map.of(
            "USER", List.of(new RequestMatcher(GET, "/books"), new RequestMatcher(DELETE, "/rental/*"), new RequestMatcher(POST, "/rental/book")),
            "MANAGER", List.of(new RequestMatcher(POST, "/book"), new RequestMatcher(GET, "/rental/books"), new RequestMatcher(GET, "/users")
                    , new RequestMatcher(GET, "/books")),
            "NO_ROLE", List.of(new RequestMatcher(POST, "/user/login"), new RequestMatcher(POST, "/user"))
    );

    private final Map<AuthorizationMatcher, RequestMatcher> dynamicAuthorizationMatcher = Map.of(
            new UserIdAuthorizationMatcher(), new RequestMatcher(GET, "/user/*/rental")
    );

    public List<AuthorizationEntry> build() {
        List<AuthorizationEntry> authorizationEntries = new ArrayList<>();

        for (Map.Entry<String, List<RequestMatcher>> entry : accessUris.entrySet()) {
            String role = entry.getKey();
            for (RequestMatcher requestMatcher : entry.getValue()) {
                authorizationEntries.add(new AuthorizationEntry(requestMatcher, new RoleAuthorizationMatcher(role)));
            }
        }

        for (Map.Entry<AuthorizationMatcher, RequestMatcher> entry : dynamicAuthorizationMatcher.entrySet()) {
            authorizationEntries.add(new AuthorizationEntry(entry.getValue(), entry.getKey()));
        }

        return authorizationEntries;
    }
}