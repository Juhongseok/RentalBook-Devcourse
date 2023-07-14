package com.jhs.rentbook.global.filter.matcher;

import com.jhs.rentbook.global.filter.matcher.AuthenticationStorage.StorageField;
import com.jhs.rentbook.global.filter.matcher.authorization.AuthorizationMatcher;
import com.jhs.rentbook.global.filter.matcher.request.RequestMatcher;
import com.jhs.rentbook.global.filter.matcher.request.RequestMatcherRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorizationManager {

    private final List<AuthorizationEntry> authorizationEntries;

    public AuthorizationManager(RequestMatcherRegistry registry) {
        this.authorizationEntries = registry.build();
    }

    public boolean canAccess(HttpServletRequest request, StorageField storageField) {
        for (AuthorizationEntry authorizationEntry : authorizationEntries) {
            RequestMatcher requestMatcher = authorizationEntry.requestMatcher();
            if (requestMatcher.isMatch(request)) {
                AuthorizationMatcher authorizationMatcher = authorizationEntry.authorizationMatcher();

                return authorizationMatcher.isGrant(request, storageField);
            }
        }

        return false;
    }

    public record AuthorizationEntry(RequestMatcher requestMatcher, AuthorizationMatcher authorizationMatcher) {}
}
