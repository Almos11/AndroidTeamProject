package com.example.demo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class User {
    private String username;
    private String token;
    private final Instant creationTime;

    public User(String username, String token) {
        this.username = username;
        this.token = token;
        this.creationTime = Instant.now();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public Instant getTokenCreationTime() {
        return creationTime;
    }

    public boolean isTokenExpired(int tokenExpirationDays) {
        Instant expirationTime = creationTime.plus(tokenExpirationDays, ChronoUnit.DAYS);
        return Instant.now().isAfter(expirationTime);
    }

    public void setToken(String token) {
        this.token = token;
    }
}

