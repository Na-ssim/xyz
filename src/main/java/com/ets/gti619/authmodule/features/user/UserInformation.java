package com.ets.gti619.authmodule.features.user;

import com.ets.gti619.authmodule.features.role.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@AllArgsConstructor
public class UserInformation implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        for(Role current: user.getRole()) {
            roles.add(new SimpleGrantedAuthority(current.getName()));
        }
        return roles;
    }

    @Override
    public String getPassword() {
        return user != null ? user.getPassword() : "";
    }

    @Override
    public String getUsername() {
        return user != null ? user.getUsername() : "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user != null && user.isAccountNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

