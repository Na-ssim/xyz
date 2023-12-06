package com.ets.gti619.authmodule.features.role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {

    private RoleRepository roleRepository;

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    public boolean roleExists(String roleName) {
        return roleRepository.findByName(roleName).isPresent();
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> findRoleById(long id) {
        return roleRepository.findById(id);
    }
}
