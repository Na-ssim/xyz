package com.ets.gti619.authmodule.controller;

import com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParametersService;
import com.ets.gti619.authmodule.features.passwordconstraint.PasswordConstraintService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParameters.*;
import static com.ets.gti619.authmodule.features.passwordconstraint.PasswordConstraints.*;


@Controller
@AllArgsConstructor
public class LoginController {

    private ConfigurableParametersService configurableParametersService;
    private PasswordConstraintService passwordConstraintService;

    @RequestMapping("/")
    public String landingPage() {
        return "login.html";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login.html";
    }

    @GetMapping("/login-failed")
    public String getFailedLoginPage(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }

    @GetMapping("/locked-out")
    public String getAccountLockedOutPage(Model model) {
        model.addAttribute("lockedOut", true);
        return "login.html";
    }

    @GetMapping("/too-many-logins")
    public String getTooManyLoginAttemptsPage(Model model, @RequestParam int seconds) {
        model.addAttribute("tooManyLogins", true);
        model.addAttribute("seconds", seconds);
        return "login.html";
    }

    @RequestMapping("/config")
    public String adminConfigPage(RedirectAttributes redirectModel, Model model) {
        int maxLoginAttempts = configurableParametersService.getParameterValue(MAX_LOGIN_ATTEMPTS);
        int timeBetweenLoginAttempts = configurableParametersService.getParameterValue(TIME_BETWEEN_LOGIN_ATTEMPTS);
        int unusableNumberOfPasswords = configurableParametersService.getParameterValue(UNUSABLE_NUMBER_OF_PREVIOUS_PASSWORDS);
        int timeBeforePasswordExpires = configurableParametersService.getParameterValue(TIME_BEFORE_PASSWORD_EXPIRES);

        int minLc = passwordConstraintService.getPasswordConstraintTargetValue(MIN_LOWERCASE);
        int minUc = passwordConstraintService.getPasswordConstraintTargetValue(MIN_UPPERCASE);
        int minLen = passwordConstraintService.getPasswordConstraintTargetValue(MIN_LENGTH);
        int minNan = passwordConstraintService.getPasswordConstraintTargetValue(MIN_NON_ALPHA_NUMERIC);
        int minNum = passwordConstraintService.getPasswordConstraintTargetValue(MIN_NUMBER);

        model.addAttribute(MAX_LOGIN_ATTEMPTS.getParamName(), maxLoginAttempts);
        model.addAttribute(TIME_BETWEEN_LOGIN_ATTEMPTS.getParamName(), timeBetweenLoginAttempts);
        model.addAttribute(UNUSABLE_NUMBER_OF_PREVIOUS_PASSWORDS.getParamName(), unusableNumberOfPasswords);
        model.addAttribute(TIME_BEFORE_PASSWORD_EXPIRES.getParamName(), timeBeforePasswordExpires);

        model.addAttribute(MIN_LOWERCASE.getFrontEndId(), minLc);
        model.addAttribute(MIN_UPPERCASE.getFrontEndId(), minUc);
        model.addAttribute(MIN_LENGTH.getFrontEndId(), minLen);
        model.addAttribute(MIN_NON_ALPHA_NUMERIC.getFrontEndId(), minNan);
        model.addAttribute(MIN_NUMBER.getFrontEndId(), minNum);

        return "config.html";
    }

}
