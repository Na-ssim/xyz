package com.ets.gti619.authmodule.features.passwordhistory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    List<PasswordHistory> findByUsernameOrderByCreatedAtDesc(String username, Pageable pageable);
}
