package com.example.myvietnampost.model;

import java.util.UUID;

public class RandomIdGenerator {

    public static String generateRandomId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}