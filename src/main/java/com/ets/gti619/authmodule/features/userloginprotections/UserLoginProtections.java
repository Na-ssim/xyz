package com.ets.gti619.authmodule.features.userloginprotections;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "userLoginProtections")
@Getter
@Setter
@NoArgsConstructor
public class UserLoginProtections {

    @Id
    @SequenceGenerator(
            name = "protections_sequence",
            sequenceName = "protections_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "protections_sequence"
    )
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column
    private String username;

    @Column
    private int failedAttempts;

    @Column
    private LocalDateTime passwordExpiryDate;

    @Column
    private LocalDateTime lastFailedLoginAttempt;

    public UserLoginProtections(String username, int failedAttempts, LocalDateTime passwordExpiryDate, LocalDateTime lastFailedLoginAttempt) {
        this.username = username;
        this.failedAttempts = failedAttempts;
        this.passwordExpiryDate = passwordExpiryDate;
        this.lastFailedLoginAttempt = lastFailedLoginAttempt;
    }

    public void incrementFailedLoginAttempts() {
        this.failedAttempts++;
    }
}
