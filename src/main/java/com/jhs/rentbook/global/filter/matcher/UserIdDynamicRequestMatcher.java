package com.jhs.rentbook.global.filter.matcher;

import com.jhs.rentbook.global.filter.AuthenticationStorage.StorageField;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

public class UserIdDynamicRequestMatcher extends RequestMatcher{

    public UserIdDynamicRequestMatcher(HttpMethod httpMethod, String uri) {
        super(httpMethod, uri);
    }

    @Override
    public boolean isMatch(HttpServletRequest request, StorageField field) {
        Long userId = field.userId();
        String uri = request.getRequestURI();

        String[] uriParam = uri.split("/");
        boolean isMatchId = false;
        for (int i = 0; i < uriParam.length; i++) {
            if (uriParam[i].equals("user")) {
                isMatchId = uriParam[i + 1].equals(String.valueOf(userId));
                break;
            }
        }

        return super.isMatch(request, field) && isMatchId;
    }
}
