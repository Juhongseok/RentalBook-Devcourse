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
            "USER", List.of(new RequestMatcher(POST, "/books/*/rentals")),
            "MANAGER", List.of(new RequestMatcher(POST, "/books"), new RequestMatcher(GET, "/users")
                    , new RequestMatcher(GET, "/books/rentals")),
            "NO_ROLE", List.of(new RequestMatcher(POST, "/users/login"), new RequestMatcher(POST, "/users"), new RequestMatcher(GET, "/books"))
    );

    private final Map<AuthorizationMatcher, RequestMatcher> dynamicAuthorizationMatcher = Map.of(
            new UserIdAuthorizationMatcher(), new RequestMatcher(DELETE, "/books/*/rentals/*/users/*"),
            new UserIdAuthorizationMatcher(), new RequestMatcher(GET, "/books/rentals/users/*")
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
