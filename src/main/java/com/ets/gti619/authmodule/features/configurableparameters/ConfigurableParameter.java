package com.ets.gti619.authmodule.features.configurableparameters;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "configurableParameters")
@Getter
@Setter
@NoArgsConstructor
public class ConfigurableParameter {

    @Id
    @SequenceGenerator(
            name = "param_sequence",
            sequenceName = "param_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "param_sequence"
    )
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column
    private String name;

    @Column
    private int savedValue;

    public ConfigurableParameter(String name, int value) {
        this.name = name;
        this.savedValue = value;
    }
}
