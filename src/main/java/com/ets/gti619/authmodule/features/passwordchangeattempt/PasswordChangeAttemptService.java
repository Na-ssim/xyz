package com.ets.gti619.authmodule.features.passwordchangeattempt;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PasswordChangeAttemptService {

    private PasswordChangeAttemptRepository repository;

    public List<PasswordChangeAttempt> getAllAttempts() {
        return repository.findAll();
    }

    public void addAttempt(PasswordChangeAttempt attempt) {
        repository.save(attempt);
    }
}
