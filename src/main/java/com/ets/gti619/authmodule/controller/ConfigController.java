package com.ets.gti619.authmodule.controller;

import com.ets.gti619.authmodule.features.client.Client;
import com.ets.gti619.authmodule.features.client.ClientService;
import com.ets.gti619.authmodule.features.clienttype.ClientType;
import com.ets.gti619.authmodule.features.clienttype.ClientTypeService;
import com.ets.gti619.authmodule.features.clienttype.ClientTypes;
import com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParametersService;
import com.ets.gti619.authmodule.features.loginattempt.LoginAttempt;
import com.ets.gti619.authmodule.features.loginattempt.LoginAttemptService;
import com.ets.gti619.authmodule.features.passwordchangeattempt.PasswordChangeAttempt;
import com.ets.gti619.authmodule.features.passwordchangeattempt.PasswordChangeAttemptService;
import com.ets.gti619.authmodule.features.passwordconstraint.PasswordConstraintService;
import com.ets.gti619.authmodule.features.user.UserService;
import com.ets.gti619.authmodule.features.userloginprotections.UserLoginProtectionsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParameters.*;
import static com.ets.gti619.authmodule.features.passwordconstraint.PasswordConstraints.*;

@Controller
@AllArgsConstructor
public class ConfigController {

    private LoginAttemptService loginAttemptService;
    private PasswordChangeAttemptService passwordChangeAttemptService;
    private ClientService clientService;
    private ClientTypeService clientTypeService;
    private ConfigurableParametersService configurableParametersService;
    private PasswordConstraintService passwordConstraintService;
    private UserService userService;
    private UserLoginProtectionsService userLoginProtectionsService;


    @RequestMapping("/events")
    public String eventsPage(Model model) {
        List<LoginAttempt> attempts = loginAttemptService.getAllAttempts();
        List<PasswordChangeAttempt> passwordChangeAttempts = passwordChangeAttemptService.getAllAttempts();
        model.addAttribute("loginAttempts", attempts);
        model.addAttribute("passwordChangeAttempts", passwordChangeAttempts);
        return "events.html";
    }

    @PostMapping("/locked_out_users")
    public String lockedOutUsers(Model model) {
        model.addAttribute("usernames", userService.getAllLockedOutUsers());
        return "locked_out_users.html";
    }

    @PostMapping("/clients_residentiels")
    public String listeClientsResident(Model model) {
        Optional<ClientType> clientType = clientTypeService.getTypeByName(ClientTypes.CLIENTS_RESIDENTIELS.getType());
        String typeTitle = ClientTypes.CLIENTS_RESIDENTIELS.getTableTitle();

        if (clientType.isPresent()) {
            List<Client> info = clientService.getClientsByType(clientType.get());
            model.addAttribute("clientInfo", info);
            model.addAttribute("typeTitle", typeTitle);
        }
        return "clients.html";
    }

    @PostMapping("/clients_affaire")
    public String listeClientsAffaire(Model model) {
            Optional<ClientType> clientType = clientTypeService.getTypeByName(ClientTypes.CLIENTS_AFFAIRE.getType());
            String typeTitle = ClientTypes.CLIENTS_AFFAIRE.getTableTitle();

        if (clientType.isPresent()) {
            List<Client> info = clientService.getClientsByType(clientType.get());
            model.addAttribute("clientInfo", info);
            model.addAttribute("typeTitle", typeTitle);
        }
        return "clients.html";
    }

    @PostMapping("/config_params")
    public String updateParams(@RequestBody MultiValueMap<String, String> formData, RedirectAttributes model){

        int maxAttempts = Integer.parseInt(String.valueOf(formData.get(MAX_LOGIN_ATTEMPTS.getParamName()).get(0)));
        configurableParametersService.updateConfigurableParameter(MAX_LOGIN_ATTEMPTS, maxAttempts);

        int secondsBetweenAttempts = Integer.parseInt(formData.get(TIME_BETWEEN_LOGIN_ATTEMPTS.getParamName()).get(0));
        configurableParametersService.updateConfigurableParameter(TIME_BETWEEN_LOGIN_ATTEMPTS, secondsBetweenAttempts);

        int unusableNumberOfPasswords = Integer.parseInt(formData.get(UNUSABLE_NUMBER_OF_PREVIOUS_PASSWORDS.getParamName()).get(0));
        configurableParametersService.updateConfigurableParameter(UNUSABLE_NUMBER_OF_PREVIOUS_PASSWORDS, unusableNumberOfPasswords);

        int timeBeforePasswordExpires = Integer.parseInt(formData.get(TIME_BEFORE_PASSWORD_EXPIRES.getParamName()).get(0));
        configurableParametersService.updateConfigurableParameter(TIME_BEFORE_PASSWORD_EXPIRES, timeBeforePasswordExpires);
        userLoginProtectionsService.updatePasswordExpiryDateForAllUsers(userService.getAllUsernames());

        int minLc = Integer.parseInt(formData.get(MIN_LOWERCASE.getFrontEndId()).get(0));
        passwordConstraintService.updateConstraint(MIN_LOWERCASE, minLc);

        int minUc = Integer.parseInt(formData.get(MIN_UPPERCASE.getFrontEndId()).get(0));
        passwordConstraintService.updateConstraint(MIN_UPPERCASE, minUc);

        int minLen = Integer.parseInt(formData.get(MIN_LENGTH.getFrontEndId()).get(0));
        passwordConstraintService.updateConstraint(MIN_LENGTH, minLen);

        int minNan = Integer.parseInt(formData.get(MIN_NON_ALPHA_NUMERIC.getFrontEndId()).get(0));
        passwordConstraintService.updateConstraint(MIN_NON_ALPHA_NUMERIC, minNan);

        int minNum = Integer.parseInt(formData.get(MIN_NUMBER.getFrontEndId()).get(0));
        passwordConstraintService.updateConstraint(MIN_NUMBER, minNum);

        model.addFlashAttribute("showMessage", true);
        model.addFlashAttribute("messages", Collections.singletonList("Parameters Updated Successfully!"));
        return "redirect:config";
    }
}
