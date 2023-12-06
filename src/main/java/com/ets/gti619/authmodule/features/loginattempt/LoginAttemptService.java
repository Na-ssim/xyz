package com.ets.gti619.authmodule.features.loginattempt;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoginAttemptService {

    private LoginAttemptRepository repository;

    public void addLoginAttempt(LoginAttempt attempt) {
        repository.save(attempt);
    }

    public List<LoginAttempt> getAllAttempts() {
        return repository.findAll();
    }
}
