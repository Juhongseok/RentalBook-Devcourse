package com.jhs.rentbook.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhs.rentbook.global.exception.ExceptionResponse;
import com.jhs.rentbook.global.exception.custom.BusinessException;
import com.jhs.rentbook.global.exception.custom.filter.AuthorizationException;
import jakarta.servlet.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class ExceptionHandlerFilter implements Filter {

    private final ObjectMapper mapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            chain.doFilter(request, response);
        } catch (BusinessException exception) {
            responseException(BAD_REQUEST, exception.getMessage(), response);
        } catch (AuthorizationException exception) {
            responseException(FORBIDDEN, exception.getMessage(), response);
        }
    }

    private void responseException(HttpStatus status, String message, ServletResponse response) throws IOException {
        ExceptionResponse exceptionResponse = new ExceptionResponse(status.value(), message);
        String responseMessage = mapper.writeValueAsString(exceptionResponse);

        response.getWriter().write(responseMessage);
    }
}
