package com.jhs.rentbook.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhs.rentbook.global.exception.ExceptionResponse;
import com.jhs.rentbook.global.exception.custom.BusinessException;
import com.jhs.rentbook.global.exception.custom.filter.AuthorizationException;
import jakarta.servlet.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class ExceptionHandlerFilter implements Filter {

    private final ObjectMapper mapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String responseMessage = "";
        try {
            chain.doFilter(request, response);
        } catch (BusinessException exception) {
            ExceptionResponse value = new ExceptionResponse(BAD_REQUEST.value(), exception.getMessage());
            responseMessage = mapper.writeValueAsString(value);
        } catch (AuthorizationException exception) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(FORBIDDEN.value(), exception.getMessage());
            responseMessage = mapper.writeValueAsString(exceptionResponse);
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().println(responseMessage);
    }
}
