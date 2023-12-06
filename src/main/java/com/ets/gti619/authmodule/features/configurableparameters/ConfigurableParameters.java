package com.ets.gti619.authmodule.features.configurableparameters;

import lombok.Getter;

@Getter
public enum ConfigurableParameters {

    MAX_LOGIN_ATTEMPTS("maxLoginAttempts", 3),
    TIME_BETWEEN_LOGIN_ATTEMPTS("timeBetweenLoginAttempts", -1),
    UNUSABLE_NUMBER_OF_PREVIOUS_PASSWORDS("unusableNumberOfPasswords", 3),
    TIME_BEFORE_PASSWORD_EXPIRES("timeBeforePasswordExpires", 86400);

    private final String paramName;
    private final int defaultValue;

    ConfigurableParameters(String paramName, int defaultValue) {
        this.paramName = paramName;
        this.defaultValue = defaultValue;
    }
}
