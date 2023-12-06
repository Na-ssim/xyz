package com.ets.gti619.authmodule.features.user;

import com.ets.gti619.authmodule.features.role.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "role_id_user"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> role;

    @Column
    private boolean isAccountNotLocked;

    @Column
    private boolean isPasswordExpired;

    @Column
    private boolean isTemporaryPassword;

    public User(String username, String password, Set<Role> role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isAccountNotLocked = true;
        this.isPasswordExpired = false;
        this.isTemporaryPassword = false;
    }
}
