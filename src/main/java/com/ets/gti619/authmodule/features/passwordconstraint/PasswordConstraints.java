package com.ets.gti619.authmodule.features.passwordconstraint;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

@Getter
public enum PasswordConstraints {
    MIN_LOWERCASE("Minimum Amount of Lowercase Letters", 1, "minLc", (password, count) -> {
        int total = 0;
        for (char letter: password.toCharArray()) {
            if (Character.isLowerCase(letter)) total++;
        }
        return total >= count;
    }),
    MIN_UPPERCASE("Minimum Amount of Uppercase Letters", 1, "minUc", (password, count) -> {
        int total = 0;
        for (char letter: password.toCharArray()) {
            if (Character.isUpperCase(letter)) total++;
        }
        return total >= count;
    }),
    MIN_LENGTH("Minimum Length of Password", 12, "minLen", (password, count) -> password.length() >= count),
    MIN_NON_ALPHA_NUMERIC("Minimum Amount of Non Alphanumeric Characters", 1, "minNan", (password, count) -> {
        int total = 0;
        List<Character> validCharacters = Arrays.asList('!', '?', '_', '-', '#', '$', '%', '&', '(', ')', '^', '~');
        for (char letter: password.toCharArray()) {
            if (validCharacters.contains(letter)) total++;
        }
        return total >= count;
    }),
    MIN_NUMBER("Minimum Amount of Numbers", 1, "minNum", (password, count) -> {
        int total = 0;
        for (char letter: password.toCharArray()) {
            if (Character.isDigit(letter)) total++;
        }
        return total >= count;
    });

    private final String name;
    private final int requirement;
    private final String frontEndId;
    private final BiFunction<String, Integer, Boolean> verification;

    PasswordConstraints(String name, int requirement, String feId, BiFunction<String, Integer, Boolean> verification) {
        this.name = name;
        this.requirement = requirement;
        this.frontEndId = feId;
        this.verification = verification;
    }

    public PasswordConstraint getConstraint() {
        return new PasswordConstraint(name, requirement, frontEndId);
    }

}
