package com.jhs.rentbook.global.filter.matcher.authorization;

import com.jhs.rentbook.global.filter.matcher.AuthenticationStorage.StorageField;
import jakarta.servlet.http.HttpServletRequest;

public class UserIdAuthorizationMatcher implements AuthorizationMatcher{

    @Override
    public boolean isGrant(HttpServletRequest request, StorageField field) {
        Long userId = field.userId();
        String uri = request.getRequestURI();

        String[] uriParam = uri.split("/");
        boolean isMatchId = false;
        for (int i = 0; i < uriParam.length; i++) {
            if (uriParam[i].equals("users")) {
                isMatchId = uriParam[i + 1].equals(String.valueOf(userId));
                break;
            }
        }

        return isMatchId && field.role().equals("USER");
    }
}
