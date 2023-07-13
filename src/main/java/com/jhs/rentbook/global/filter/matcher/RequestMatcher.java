package com.jhs.rentbook.global.filter.matcher;

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

    public boolean isMatch(String httpMethod, String uri) {
        boolean matchHttpMethod = this.httpMethod == null || this.httpMethod.equals(httpMethod);
        boolean matchUri = this.uriPattern.matcher(uri).matches();

        return matchHttpMethod && matchUri;
    }
}
