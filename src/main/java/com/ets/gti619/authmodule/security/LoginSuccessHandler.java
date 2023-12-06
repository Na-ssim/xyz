package com.ets.gti619.authmodule.security;

import com.ets.gti619.authmodule.features.loginattempt.LoginAttempt;
import com.ets.gti619.authmodule.features.loginattempt.LoginAttemptService;
import com.ets.gti619.authmodule.features.user.UserService;
import com.ets.gti619.authmodule.features.userloginprotections.UserLoginProtectionsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private LoginAttemptService loginAttemptService;
    private UserLoginProtectionsService userLoginProtectionsService;
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        logAttempt(request, authentication);
        String username = authentication.getName();
        if (userLoginProtectionsService.isPasswordExpired(username) || userService.doesUserHaveTemporaryPassword(username)) {
            response.sendRedirect("/reset_expired_password");
            return;
        }
        response.sendRedirect("/config");
    }

    private void logAttempt(HttpServletRequest request, Authentication authentication) {
        String username = authentication.getName();

        LoginAttempt attempt = new LoginAttempt();
        attempt.setLoginTime(LocalDateTime.now());
        attempt.setUserAgent(request.getHeader("User-Agent"));
        attempt.setUserLoggingIn(username);
        attempt.setLoginSuccess(true);
        userLoginProtectionsService.resetFailedAttempts(username);
        loginAttemptService.addLoginAttempt(attempt);
    }
}
