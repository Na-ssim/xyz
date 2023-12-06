package com.ets.gti619.authmodule.features.userloginprotections;

import com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParametersService;
import com.ets.gti619.authmodule.features.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParameters.TIME_BEFORE_PASSWORD_EXPIRES;
import static com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParameters.TIME_BETWEEN_LOGIN_ATTEMPTS;

@Service
@AllArgsConstructor
public class UserLoginProtectionsService {

    private UserLoginProtectionsRepository repository;
    private ConfigurableParametersService configurableParametersService;

    /**
     * Initializes protection parameters for a given user
     * @param username user for which to initialize protections
     */
    public void initializeUserEntry(String username) {
        int secondsBeforePasswordExpires = configurableParametersService.getParameterValue(TIME_BEFORE_PASSWORD_EXPIRES);
        UserLoginProtections entry = new UserLoginProtections(username, 0 ,LocalDateTime.now().plusSeconds(secondsBeforePasswordExpires), null);
        repository.save(entry);
    }

    /**
     * Gets the current amount of failed login attempts for a given user
     * @param username user for which to fetch the amount of failed login attempts
     * @return the integer amount of failed login attempts
     */
    public int getFailedLoginAttempts(String username) {
        Optional<UserLoginProtections> current = repository.getUserLoginProtectionsByUsername(username);
        if (current.isEmpty()) return -1;
        return current.get().getFailedAttempts();
    }

    /**
     * Increments the amount of failed login attempts for a given user
     * @param username user for which to increment the amount of failed login attempts
     */
    public void incrementFailedAttempts(String username) {
        Optional<UserLoginProtections> current = repository.getUserLoginProtectionsByUsername(username);
        if (current.isEmpty()) return;
        current.get().incrementFailedLoginAttempts();
        current.get().setLastFailedLoginAttempt(LocalDateTime.now());
        repository.save(current.get());
    }

    /**
     * Clears the amount of failed login attempts for a given user
     * @param username user for which to clear failed login attempts
     */
    public void resetFailedAttempts(String username) {
        Optional<UserLoginProtections> current = repository.getUserLoginProtectionsByUsername(username);
        if (current.isEmpty()) return;
        current.get().setFailedAttempts(0);
        current.get().setLastFailedLoginAttempt(null);
        repository.save(current.get());
    }

    /**
     * Checks whether the specified user's password is expired
     * @param username user for which to verify if the password is expired
     * @return true if password is expired, false otherwise
     */
    public boolean isPasswordExpired(String username) {
        Optional<UserLoginProtections> current = repository.getUserLoginProtectionsByUsername(username);
        if (current.isEmpty()) return false;
        return LocalDateTime.now().isAfter(current.get().getPasswordExpiryDate());
    }

    /**
     * Updates the password expiry date for the specified user
     * @param username User for which to update the password expiry date
     */
    public void updatePasswordExpiryDate(String username) {
        Optional<UserLoginProtections> current = repository.getUserLoginProtectionsByUsername(username);
        if (current.isEmpty()) return;
        int secondsBeforeExpiry = configurableParametersService.getParameterValue(TIME_BEFORE_PASSWORD_EXPIRES);
        if(secondsBeforeExpiry == -1) {
            current.get().setPasswordExpiryDate(null);
        }
        else {
            current.get().setPasswordExpiryDate(LocalDateTime.now().plusSeconds(secondsBeforeExpiry));
        }
        repository.save(current.get());
    }

    /**
     * Update password expiry date when the configurable parameter is updated
     * @param usernames list of all users to update the expiry date for
     */
    public void updatePasswordExpiryDateForAllUsers(List<String> usernames) {
        for (String user: usernames) {
            updatePasswordExpiryDate(user);
        }
    }

    /**
     * Gets the seconds remaining before the specified user can attempt to log in again
     * @param username User attempting to log in
     * @return -1 if the user cannot be found, 0 if the user can attempt to log in, else number of seconds before being able to attempt logging in
     */
    public int getSecondsBeforeNextLoginAttempt(String username) {
        Optional<UserLoginProtections> current = repository.getUserLoginProtectionsByUsername(username);
        if (current.isEmpty()) return -1;

        int secondsBeforeNextLogin = configurableParametersService.getParameterValue(TIME_BETWEEN_LOGIN_ATTEMPTS);
        LocalDateTime lastFailedAttempt = current.get().getLastFailedLoginAttempt();

        if (secondsBeforeNextLogin == -1 || lastFailedAttempt == null) return 0;
        long secondsNow = System.currentTimeMillis() / 1000;
        long secondsAtFailedLogin = lastFailedAttempt.atZone(ZoneId.systemDefault()).toEpochSecond();
        int secondsBetweenNowAndFailure = Long.valueOf(secondsNow - secondsAtFailedLogin).intValue();
        return Math.max(0, secondsBeforeNextLogin - secondsBetweenNowAndFailure);
    }
}
