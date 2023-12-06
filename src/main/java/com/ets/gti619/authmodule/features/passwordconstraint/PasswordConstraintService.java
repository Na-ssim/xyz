package com.ets.gti619.authmodule.features.passwordconstraint;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PasswordConstraintService {

    private PasswordConstraintsRepository repository;

    public void updateConstraint(PasswordConstraints passwordConstraint, int target) {
        Optional<PasswordConstraint> constraint = repository.getPasswordConstraintByName(passwordConstraint.getName());
        if (constraint.isEmpty()) return;
        constraint.get().setTargetValue(target);
        repository.save(constraint.get());
    }

    public int getPasswordConstraintTargetValue(PasswordConstraints passwordConstraint) {
        Optional<PasswordConstraint> constraint = repository.getPasswordConstraintByName(passwordConstraint.getName());
        if (constraint.isEmpty()) return passwordConstraint.getRequirement();
        return constraint.get().getTargetValue();
    }

    public boolean verifyConstraints(String password) {
        for(PasswordConstraints constraint: PasswordConstraints.values()) {
            Optional<PasswordConstraint> base = repository.getPasswordConstraintByName(constraint.getName());
            if (base.isEmpty() || !constraint.getVerification().apply(password, base.get().getTargetValue())) return false;
        }
        return true;
    }

    public List<PasswordConstraint> getAllConstraints() {
        return repository.findAll();
    }

    public void initializeConstraints() {
        for(PasswordConstraints constraint: PasswordConstraints.values()) {
            Optional<PasswordConstraint> potential = repository.getPasswordConstraintByName(constraint.getName());
            if(potential.isEmpty()) repository.save(constraint.getConstraint());
        }
    }
}
