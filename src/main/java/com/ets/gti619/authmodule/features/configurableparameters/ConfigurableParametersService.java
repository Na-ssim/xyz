package com.ets.gti619.authmodule.features.configurableparameters;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfigurableParametersService {

    private ConfigurableParametersRepository repository;

    public void updateConfigurableParameter(ConfigurableParameters parameter, int value) {
        Optional<ConfigurableParameter> param = repository.getConfigurableParameterByName(parameter.getParamName());
        if (param.isEmpty()) return;
        param.get().setSavedValue(value);
        repository.save(param.get());
    }

    public int getParameterValue(ConfigurableParameters parameter) {
        Optional<ConfigurableParameter> param = repository.getConfigurableParameterByName(parameter.getParamName());
        if (param.isEmpty()) return parameter.getDefaultValue();
        return param.get().getSavedValue();
    }

    public void initializeValues() {
        for(ConfigurableParameters param: ConfigurableParameters.values()) {
            ConfigurableParameter newParam = new ConfigurableParameter(param.getParamName(), param.getDefaultValue());
            Optional<ConfigurableParameter> potential = repository.getConfigurableParameterByName(param.getParamName());
            if (potential.isEmpty()) repository.save(newParam);
        }
    }
}
