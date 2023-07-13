package com.jhs.rentbook.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhs.rentbook.global.filter.AuthenticationFilter;
import com.jhs.rentbook.global.filter.AuthenticationStorage;
import com.jhs.rentbook.global.filter.AuthorizationFilter;
import com.jhs.rentbook.global.filter.ExceptionHandlerFilter;
import com.jhs.rentbook.repository.UserRepository;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<Filter> exceptionHandlerFilter(ObjectMapper mapper) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new ExceptionHandlerFilter(mapper));
        registration.setOrder(1);
        registration.addUrlPatterns("/*");

        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> authenticationFilter(AuthenticationStorage storage, UserRepository repository) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new AuthenticationFilter(storage, repository));
        registration.setOrder(2);
        registration.addUrlPatterns("/*");

        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> authorizationFilter(AuthenticationStorage storage) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new AuthorizationFilter(storage));
        registration.setOrder(3);
        registration.addUrlPatterns("/*");

        return registration;
    }

}
