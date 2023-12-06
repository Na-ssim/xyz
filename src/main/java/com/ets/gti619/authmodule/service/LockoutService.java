package com.ets.gti619.authmodule.service;


import com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParametersService;
import com.ets.gti619.authmodule.features.userloginprotections.UserLoginProtectionsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParameters.MAX_LOGIN_ATTEMPTS;

@Service
@AllArgsConstructor
public class LockoutService {

    private static ConfigurableParametersService configurableParametersService;
    private static UserLoginProtectionsService userLoginProtectionsService;

    public static boolean canUserLogin(String username) {
        int attempts = userLoginProtectionsService.getFailedLoginAttempts(username);
        int maxAttempts = configurableParametersService.getParameterValue(MAX_LOGIN_ATTEMPTS);
        if (attempts == -1) return false;
        return attempts <= maxAttempts;
    }
}
