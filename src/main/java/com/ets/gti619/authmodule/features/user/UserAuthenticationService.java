package com.ets.gti619.authmodule.features.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserAuthenticationService implements UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return new UserInformation(user.orElse(null));
    }

    public boolean correctPassword(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) return false;
        return passwordEncoder.matches(password, user.get().getPassword());
    }
}
