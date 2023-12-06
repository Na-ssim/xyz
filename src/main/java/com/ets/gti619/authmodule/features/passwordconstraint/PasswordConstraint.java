package com.ets.gti619.authmodule.features.passwordconstraint;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "password_constraints")
@NoArgsConstructor
@Getter
public class PasswordConstraint {

    @SequenceGenerator(
            name = "password_constraint_sequence",
            sequenceName = "password_constraint_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "password_constraint_sequence"
    )
    @Id
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "target")
    @Setter(AccessLevel.PUBLIC)
    private int targetValue;

    @Column(name = "fe_id")
    private String feId;

    public PasswordConstraint(String name, int targetValue, String feId) {
        this.name = name;
        this.targetValue = targetValue;
        this.feId = feId;
    }
}
