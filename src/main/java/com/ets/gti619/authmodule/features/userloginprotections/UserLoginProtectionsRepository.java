package com.ets.gti619.authmodule.features.userloginprotections;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginProtectionsRepository extends JpaRepository<UserLoginProtections, Long> {

    Optional<UserLoginProtections> getUserLoginProtectionsByUsername(String username);
}
