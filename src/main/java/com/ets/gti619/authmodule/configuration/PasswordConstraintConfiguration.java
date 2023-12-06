package com.ets.gti619.authmodule.configuration;

import com.ets.gti619.authmodule.features.passwordconstraint.PasswordConstraintService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordConstraintConfiguration {

    @Bean
    CommandLineRunner passwordConstraintConfig(PasswordConstraintService service) {
        return args -> {
            service.initializeConstraints();
        };
    }
}
