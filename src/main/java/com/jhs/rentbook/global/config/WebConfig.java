package com.jhs.rentbook.global.config;

import com.jhs.rentbook.global.filter.AuthenticationFilter;
import com.jhs.rentbook.global.filter.AuthorizationFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<Filter> authenticationFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new AuthenticationFilter());
        registration.setOrder(1);
        registration.addUrlPatterns("/*");

        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> authorizationFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();

        registration.setFilter(new AuthorizationFilter());
        registration.setOrder(2);
        registration.addUrlPatterns("/*");

        return registration;
    }

}
