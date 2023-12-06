package com.ets.gti619.authmodule.features.passwordhistory;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PasswordHistoryService {

    private PasswordHistoryRepository passwordHistoryRepository;
    private PasswordEncoder passwordEncoder;

    public void addPassword(PasswordHistory passwordHistory) {
        passwordHistory.setPassword(passwordEncoder.encode(passwordHistory.getPassword()));
        passwordHistoryRepository.save(passwordHistory);
    }
    
    public boolean UsedPassword(String username, String password, int nbrLastPasswords) {
        
        List<PasswordHistory> passwordHistories = passwordHistoryRepository.findByUsernameOrderByCreatedAtDesc(username, PageRequest.of(0, nbrLastPasswords));

        for (PasswordHistory passwordHistory: passwordHistories) {
             if(passwordEncoder.matches(password, passwordHistory.getPassword())){
                 return true;
             };
        }
        return false;
    }

}
