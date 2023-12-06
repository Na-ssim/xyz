package com.ets.gti619.authmodule.configuration;

import com.ets.gti619.authmodule.features.configurableparameters.ConfigurableParametersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurableParametersConfiguration {

    @Bean
    CommandLineRunner paramConfig(ConfigurableParametersService service) {
        return args -> {
            service.initializeValues();
        };
    }
}
