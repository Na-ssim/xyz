package com.ets.gti619.authmodule.features.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * Add a new user to the system
     * @param user User object to be persisted
     */
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Updates a given user's password
     * @param username user for which to update password
     * @param password updated password
     * @return true if password was updated, false otherwise
     */
    public boolean updateUserPassword(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) return false;
        user.get().setPassword(passwordEncoder.encode(password));
        userRepository.save(user.get());
        return true;
    }

    /**
     * Marks a user as being locked out or not
     * @param username user for which to toggle lock out status
     * @param isUserLockedOut boolean value for whether user is locked out or not
     */
    public void setUserLockedOut(String username, boolean isUserLockedOut) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) return;
        user.get().setAccountNotLocked(!isUserLockedOut);
        userRepository.save(user.get());
    }

    /**
     * Gets a list of all users that are currently locked out of the system
     * @return a list of the usernames of locked out users
     */
    public List<String> getAllLockedOutUsers() {
        List<User> lockedOut = userRepository.findAllByIsAccountNotLockedFalse();
        return lockedOut.stream().map(User::getUsername).collect(Collectors.toList());
    }

    /**
     * Gets a list of all users in the system
     * @return a list of all existing usernames
     */
    public List<String> getAllUsernames() {
        return userRepository.findAll().stream().map(User::getUsername).collect(Collectors.toList());
    }

    /**
     * Verifies whether an individual user is locked out
     * @param username user for which we are verifying whether they are locked out or not
     * @return true if user is locked out, false otherwise
     */
    public boolean isUserLockedOut(String username) {
        return getAllLockedOutUsers().contains(username);
    }

    /**
     * Updates whether a given user has a temporary password in place
     * @param username user for which we are indicating a temporary password or not
     * @param doesUserHaveTemporaryPassword boolean value indicating whether the specified user has a temporary password
     */
    public void setDoesUserHaveTemporaryPassword(String username, boolean doesUserHaveTemporaryPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) return;
        user.get().setTemporaryPassword(doesUserHaveTemporaryPassword);
        userRepository.save(user.get());
    }

    /**
     * Checks whether the specified user has a temporary password
     * @param username user for which to check if a temporary password is set
     * @return true if the user has a temporary password, false otherwise
     */
    public boolean doesUserHaveTemporaryPassword(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) return false;
        return user.get().isTemporaryPassword();
    }

}
