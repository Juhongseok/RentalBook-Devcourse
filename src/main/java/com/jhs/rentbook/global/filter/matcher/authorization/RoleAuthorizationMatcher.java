package com.jhs.rentbook.global.filter.matcher.authorization;

import com.jhs.rentbook.global.filter.matcher.AuthenticationStorage.StorageField;
import jakarta.servlet.http.HttpServletRequest;

public class RoleAuthorizationMatcher implements AuthorizationMatcher{

    private final String role;

    public RoleAuthorizationMatcher(String role) {
        this.role = role;
    }

    public boolean isGrant(HttpServletRequest request, StorageField field) {
        return field.role().equals(role) || role.equals("NO_ROLE");
    }
}
