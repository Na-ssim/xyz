package com.ets.gti619.authmodule.configuration;

import com.ets.gti619.authmodule.features.role.Role;
import com.ets.gti619.authmodule.features.role.RoleService;
import com.ets.gti619.authmodule.features.role.Roles;
import com.ets.gti619.authmodule.features.user.User;
import com.ets.gti619.authmodule.features.user.UserAuthenticationService;
import com.ets.gti619.authmodule.features.user.UserService;
import com.ets.gti619.authmodule.features.userloginprotections.UserLoginProtectionsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class UserConfiguration {

    private final UserAuthenticationService userAuthenticationService;

    private final User admin;
    private final User prepClientsResidentiels;
    private final User prepClientsAffaire;

    private final Role adminRole;
    private final Role prepClientsResidentielsRole;
    private final Role prepClientsAffaireRole;

    public UserConfiguration(UserAuthenticationService userAuthenticationService) {

        this.userAuthenticationService = userAuthenticationService;

        adminRole = new Role(Roles.ADMIN.getRole());
        prepClientsResidentielsRole = new Role(Roles.PREP_CLIENTS_RESIDENTIELS.getRole());
        prepClientsAffaireRole = new Role(Roles.PREP_CLIENTS_AFFAIRE.getRole());

        admin = new User("Administrateur", "mdpAdmin", Collections.singleton(adminRole));
        prepClientsResidentiels = new User("Utilisateur1", "mdpUser1", Collections.singleton(prepClientsResidentielsRole));
        prepClientsAffaire = new User("Utilisateur2", "mdpUser2", Collections.singleton(prepClientsAffaireRole));
    }

    @Bean
    CommandLineRunner userConfig(UserService userService, RoleService roleService, UserLoginProtectionsService userLoginProtectionsService) {
        return args -> {
            if (!roleService.roleExists(adminRole.getName())) {
                roleService.addRole(adminRole);
            }
            if (!roleService.roleExists(prepClientsResidentielsRole.getName())) {
                roleService.addRole(prepClientsResidentielsRole);
            }
            if (!roleService.roleExists(prepClientsAffaireRole.getName())) {
                roleService.addRole(prepClientsAffaireRole);
            }

            if (userAuthenticationService.loadUserByUsername(admin.getUsername()).getUsername().equals("")) {
                userService.addUser(admin);
                userLoginProtectionsService.initializeUserEntry("Administrateur");
            }
            if (userAuthenticationService.loadUserByUsername(prepClientsResidentiels.getUsername()).getUsername().equals("")) {
                userService.addUser(prepClientsResidentiels);
                userLoginProtectionsService.initializeUserEntry("Utilisateur1");
            }
            if (userAuthenticationService.loadUserByUsername(prepClientsAffaire.getUsername()).getUsername().equals("")) {
                userService.addUser(prepClientsAffaire);
                userLoginProtectionsService.initializeUserEntry("Utilisateur2");
            }
        };
    }
}
