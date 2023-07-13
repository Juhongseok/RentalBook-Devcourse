package com.jhs.rentbook.global.filter;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationStorage {

    private ThreadLocal<String> storage = new ThreadLocal<>();

    public void put(String role) {
        storage.set(role);
    }

    public String get() {
        String role = storage.get();
        if (role == null) {
            return "NO_ROLE";
        }

        return role;
    }
}
