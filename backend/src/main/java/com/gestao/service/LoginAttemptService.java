package com.gestao.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoginAttemptService {
    private final int  MAX_ATTEMPTS = 5;
    private final ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();

    public void loginSucceeded(String key) {
        attemptsCache.remove(key);
    }

    public void loginFailed(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attemptsCache.put(key, attempts + 1);
    }

    public boolean isblocked(String key) {
        return attemptsCache.getOrDefault(key, 0) >= MAX_ATTEMPTS;
    }

}
