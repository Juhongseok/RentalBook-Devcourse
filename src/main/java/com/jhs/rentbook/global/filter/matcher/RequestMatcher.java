package com.jhs.rentbook.global.filter.matcher;

import com.jhs.rentbook.global.filter.AuthenticationStorage.StorageField;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

import java.util.regex.Pattern;

public class RequestMatcher {
    private String httpMethod;
    private final Pattern uriPattern;

    public RequestMatcher(HttpMethod httpMethod, String uri) {
        this.httpMethod = httpMethod.name();
        this.uriPattern = toPattern(uri);
    }

    private Pattern toPattern(String uri) {
        return Pattern.compile(uri.replace("*", ".*[^/]").concat("$"));
    }

    public boolean isMatch(HttpServletRequest request, StorageField field) {
        boolean matchHttpMethod = this.httpMethod == null || this.httpMethod.equals(request.getMethod());
        boolean matchUri = this.uriPattern.matcher(request.getRequestURI()).matches();

        return matchHttpMethod && matchUri;
    }
}
