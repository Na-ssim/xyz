package com.ets.gti619.authmodule.features.passwordhistory;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "PasswordHistory")
@Getter
@Setter
@NoArgsConstructor
public class PasswordHistory {
    @Id
    @SequenceGenerator(
            name = "history_password_sequence",
            sequenceName = "history_password_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "history_password_sequence"
    )
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public PasswordHistory(String username, String password, LocalDateTime createdAt) {
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
    }
}
