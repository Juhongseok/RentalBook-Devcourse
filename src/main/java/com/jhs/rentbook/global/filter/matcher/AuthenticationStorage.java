package com.jhs.rentbook.global.filter.matcher;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationStorage {

    private ThreadLocal<StorageField> storage = new ThreadLocal<>();

    public void put(StorageField field) {
        storage.set(field);
    }

    public StorageField get() {
        StorageField field = storage.get();
        if (field == null) {
            return new StorageField(null, "NO_ROLE");
        }

        return field;
    }

    public record StorageField(Long userId, String role) {
    }
}
