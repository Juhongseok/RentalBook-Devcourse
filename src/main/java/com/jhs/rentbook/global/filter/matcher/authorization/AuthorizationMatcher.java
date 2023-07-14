package com.jhs.rentbook.global.filter.matcher.authorization;

import com.jhs.rentbook.global.filter.AuthenticationStorage.StorageField;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthorizationMatcher {

    boolean isGrant(HttpServletRequest request, StorageField field);

}
