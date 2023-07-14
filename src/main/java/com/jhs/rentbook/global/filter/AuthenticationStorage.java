package com.jhs.rentbook.global.filter;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationStorage {

    private ThreadLocal<StorageField> storage = new ThreadLocal<>();

    public void put(StorageField field) {
        storage.set(field);
    }

    public StorageField get() {
        return storage.get();
    }

    public record StorageField(Long userId, String role) {
    }
}
