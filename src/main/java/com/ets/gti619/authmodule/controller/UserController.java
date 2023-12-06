package com.ets.gti619.authmodule.controller;

import com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParameters;
import com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParametersService;
import com.ets.gti619.authmodule.features.passwordchangeattempt.PasswordChangeAttempt;
import com.ets.gti619.authmodule.features.passwordchangeattempt.PasswordChangeAttemptService;
import com.ets.gti619.authmodule.features.passwordconstraint.PasswordConstraint;
import com.ets.gti619.authmodule.features.passwordconstraint.PasswordConstraintService;
import com.ets.gti619.authmodule.features.passwordhistory.PasswordHistory;
import com.ets.gti619.authmodule.features.passwordhistory.PasswordHistoryService;
import com.ets.gti619.authmodule.features.role.Role;
import com.ets.gti619.authmodule.features.role.RoleService;
import com.ets.gti619.authmodule.features.user.User;
import com.ets.gti619.authmodule.features.user.UserAuthenticationService;
import com.ets.gti619.authmodule.features.user.UserService;
import com.ets.gti619.authmodule.features.userloginprotections.UserLoginProtectionsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class UserController {

    private static final String NEW_USER_PASSWORD_KEY = "password";
    private static final String NEW_USER_USERNAME_KEY = "username";
    private static final String NEW_USER_ROLE = "roleToAssign";
    private static final String ADMIN_PASSWORD_KEY = "adminPassword";

    private static final String OLD_PASSWORD_KEY = "oldPassword";
    private static final String UPDATED_PASSWORD_KEY = "newPassword";
    private static final String CONFIRM_UPDATED_PASSWORD = "confirmNewPassword";
    //private static final int NUMBER_OF_UNUSABLE_OLD_PASSWORD = 3;

    private static final String CONFIG_REDIRECT = "redirect:config";


    private RoleService roleService;
    private UserService userService;
    private UserAuthenticationService userAuthenticationService;

    private PasswordHistoryService passwordHistoryService;
    private PasswordConstraintService passwordConstraintService;
    private PasswordChangeAttemptService passwordChangeAttemptService;
    private UserLoginProtectionsService userLoginProtectionsService;
    private ConfigurableParametersService configurableParametersService;

    @GetMapping("/new")
    public String newUserPage(Model model) {
        List<Role> roles = roleService.getAllRoles();
        List<PasswordConstraint> constraints = passwordConstraintService.getAllConstraints();
        model.addAttribute("roles", roles);
        model.addAttribute("constraints", constraints);
        return "new_user.html";
    }

    @PostMapping("/new")
    public String createNewUser(@RequestBody MultiValueMap<String, String> formData, RedirectAttributes model) {
        String newUsername = formData.get(NEW_USER_USERNAME_KEY).get(0);
        String newPassword = formData.get(NEW_USER_PASSWORD_KEY).get(0);
        String adminPassword = formData.get(ADMIN_PASSWORD_KEY).get(0);
        Optional<Role> newRole = roleService.findRoleById(Long.parseLong(formData.get(NEW_USER_ROLE).get(0)));

        List<String> messages = new ArrayList<>();
        model.addFlashAttribute("showMessage", true);
        if (!userAuthenticationService.correctPassword("Administrateur", adminPassword)) {
            messages.add("Could not authenticate");
        }
        if (newRole.isEmpty()) {
            messages.add("Role could not be found");
        }
        if (!passwordConstraintService.verifyConstraints(newPassword)) {
            messages.add("Password did not meet security requirements");
        }
        if (userService.getAllUsernames().contains(newUsername)) {
            messages.add("User already created!");
        }

        if (messages.isEmpty()) {
            User newUser = new User(newUsername, newPassword, Collections.singleton(newRole.get()));
            userService.addUser(newUser);
            messages.add("User successfully created!");
            userLoginProtectionsService.initializeUserEntry(newUsername);
        }
        model.addFlashAttribute("messages", messages);
        return CONFIG_REDIRECT;
    }

    @GetMapping("/update_password")
    public String getUpdatePassword(Model model) {
        List<PasswordConstraint> constraints = passwordConstraintService.getAllConstraints();
        model.addAttribute("constraints", constraints);
        return "update_password.html";
    }

    @PostMapping("/update_password")
    public String updatePassword(@RequestBody MultiValueMap<String, String> formData, RedirectAttributes model) {
        String oldPassword = formData.get(OLD_PASSWORD_KEY).get(0);
        String updatedPassword = formData.get(UPDATED_PASSWORD_KEY).get(0);
        String confirmUpdatedPassword = formData.get(CONFIRM_UPDATED_PASSWORD).get(0);
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        List<String> messages = new ArrayList<>();
        model.addFlashAttribute("showMessage", true);
        if (!userAuthenticationService.correctPassword(currentUser, oldPassword)) {
            messages.add("Could not authenticate");
        }
        if(passwordHistoryService.UsedPassword(currentUser, updatedPassword, configurableParametersService.getParameterValue(ConfigurableParameters.UNUSABLE_NUMBER_OF_PREVIOUS_PASSWORDS))){
            messages.add("Password already used");
        }
        if (!updatedPassword.equals(confirmUpdatedPassword)) {
            messages.add("Passwords did not match");
        }
        if (!messages.isEmpty()) {
            model.addFlashAttribute("messages", messages);
            passwordChangeAttemptService.addAttempt(new PasswordChangeAttempt(currentUser, LocalDateTime.now(), false));
            return CONFIG_REDIRECT;
        }

        boolean flag = false;
        if (userService.updateUserPassword(currentUser, updatedPassword)) {
            passwordHistoryService.addPassword(new PasswordHistory(currentUser, updatedPassword, LocalDateTime.now()));
            messages.add("Password updated successfully");
            flag = true;
        }
        else {
            messages.add("Could not update password");
        }
        passwordChangeAttemptService.addAttempt(new PasswordChangeAttempt(currentUser, LocalDateTime.now(), flag));
        model.addFlashAttribute("messages", messages);
        return CONFIG_REDIRECT;
    }

    @GetMapping("/reset_expired_password")
    public String getResetExpiredPasswordPage(Model model) {
        List<PasswordConstraint> constraints = passwordConstraintService.getAllConstraints();
        model.addAttribute("constraints", constraints);
        return "reset_expired_password.html";
    }

    @PostMapping("/reset_expired_password")
    public String resetExpiredPassword(@RequestBody MultiValueMap<String, String> formData, RedirectAttributes model) {
        String updatedPassword = formData.get(UPDATED_PASSWORD_KEY).get(0);
        String confirmUpdatedPassword = formData.get(CONFIRM_UPDATED_PASSWORD).get(0);
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        List<String> messages = new ArrayList<>();
        model.addFlashAttribute("showMessage", true);

        if(passwordHistoryService.UsedPassword(currentUser, updatedPassword, configurableParametersService.getParameterValue(ConfigurableParameters.UNUSABLE_NUMBER_OF_PREVIOUS_PASSWORDS))){
            messages.add("Password already used");
        }
        if (!updatedPassword.equals(confirmUpdatedPassword)) {
            messages.add("Passwords did not match");
        }
        if (!messages.isEmpty()) {
            model.addFlashAttribute("messages", messages);
            return CONFIG_REDIRECT;
        }

        if (userService.updateUserPassword(currentUser, updatedPassword)) {
            messages.add("Password updated successfully");
            userLoginProtectionsService.updatePasswordExpiryDate(currentUser);
            userService.setDoesUserHaveTemporaryPassword(currentUser, false);
        }
        else {
            messages.add("Could not update password");
            return "redirect:login";
        }

        model.addFlashAttribute("messages", messages);
        return CONFIG_REDIRECT;
    }

    @PostMapping("/temp_pwd")
    public String setTempPassword(@RequestBody MultiValueMap<String, String> formData, RedirectAttributes model) {
        String username = formData.get("username").get(0);
        String password = formData.get("pwd").get(0);

        List<String> messages = new ArrayList<>();
        if (userService.isUserLockedOut(username) && userService.updateUserPassword(username, password)) {
            messages.add("Password for " + username + " updated successfully!");
            userService.setDoesUserHaveTemporaryPassword(username, true);
            userService.setUserLockedOut(username, false);
        }
        else {
            messages.add("Could not reset password");
        }
        model.addFlashAttribute("showMessage", true);
        model.addFlashAttribute("messages", messages);
        return CONFIG_REDIRECT;
    }
}
