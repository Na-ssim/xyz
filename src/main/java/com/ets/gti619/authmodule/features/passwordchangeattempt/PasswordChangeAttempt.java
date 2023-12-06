package com.ets.gti619.authmodule.features.passwordchangeattempt;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "passwordChangeAttempts")
@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeAttempt {

    @Id
    @SequenceGenerator(
            name = "attempt_password_sequence",
            sequenceName = "attempt_password_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attempt_password_sequence"
    )
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column
    private String changingUser;

    @Column
    private LocalDateTime changeTime;

    @Column
    private boolean changeSuccess;

    public PasswordChangeAttempt(String user, LocalDateTime attemptTime, boolean changeSuccess) {
        this.changingUser = user;
        this.changeTime = attemptTime;
        this.changeSuccess = changeSuccess;
    }
}
