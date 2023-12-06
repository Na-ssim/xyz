package com.ets.gti619.authmodule.features.configurableparameters;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigurableParametersRepository extends JpaRepository<ConfigurableParameter,Long> {
    Optional<ConfigurableParameter> getConfigurableParameterByName(String name);
}
