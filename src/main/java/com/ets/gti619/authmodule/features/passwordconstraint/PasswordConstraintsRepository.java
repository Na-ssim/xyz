package com.ets.gti619.authmodule.features.passwordconstraint;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordConstraintsRepository extends JpaRepository<PasswordConstraint, Long> {
    @Override
    void deleteAll();

    Optional<PasswordConstraint> getPasswordConstraintByName(String name);
}
