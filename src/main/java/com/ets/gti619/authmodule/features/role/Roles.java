package com.ets.gti619.authmodule.features.role;

public enum Roles {
    ADMIN("Administrateur"),
    PREP_CLIENTS_RESIDENTIELS("Prepose aux clients residentiels"),
    PREP_CLIENTS_AFFAIRE("Prepose aux clients d'affaire");

    Roles(String role) {
        this.role = role;
    }

    private final String role;

    public String getRole() {
        return role;
    }
}
