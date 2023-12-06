package com.ets.gti619.authmodule.security;

import com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParametersService;
import com.ets.gti619.authmodule.features.loginattempt.LoginAttempt;
import com.ets.gti619.authmodule.features.loginattempt.LoginAttemptService;
import com.ets.gti619.authmodule.features.user.UserService;
import com.ets.gti619.authmodule.features.userloginprotections.UserLoginProtectionsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParameters.MAX_LOGIN_ATTEMPTS;

@Component
@AllArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private LoginAttemptService loginAttemptService;
    private UserLoginProtectionsService userLoginProtectionsService;
    private ConfigurableParametersService configurableParametersService;
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String username = request.getParameter("username");
        logAttempt(request, username);
        if (updateAccountLockedOutStatus(request, username)) {
            response.sendRedirect("/locked-out");
            return;
        }
        response.sendRedirect("/login-failed");
    }

    private void logAttempt(HttpServletRequest request, String username) {
        LoginAttempt attempt = new LoginAttempt();
        attempt.setLoginTime(LocalDateTime.now());
        attempt.setUserAgent(request.getHeader("User-Agent"));
        attempt.setUserLoggingIn(username);
        attempt.setLoginSuccess(false);

        loginAttemptService.addLoginAttempt(attempt);
    }

    private boolean updateAccountLockedOutStatus(HttpServletRequest request, String username) {
        userLoginProtectionsService.incrementFailedAttempts(username);
        int attempts = userLoginProtectionsService.getFailedLoginAttempts(username);
        int maxAttempts = configurableParametersService.getParameterValue(MAX_LOGIN_ATTEMPTS);

        if(attempts > maxAttempts) {
            userService.setUserLockedOut(username, true);
            return true;
        }
        return false;
    }
}
