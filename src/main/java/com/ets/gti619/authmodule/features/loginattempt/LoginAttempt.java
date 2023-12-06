package com.ets.gti619.authmodule.features.loginattempt;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "loginAttempts")
@Getter
@Setter
@NoArgsConstructor
public class LoginAttempt {

    @Id
    @SequenceGenerator(
            name = "attempt_sequence",
            sequenceName = "attempt_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attempt_sequence"
    )
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "user_logging_in")
    private String userLoggingIn;

    @Column
    private String userAgent;

    @Column
    private LocalDateTime loginTime;

    @Column
    private boolean loginSuccess;
}
