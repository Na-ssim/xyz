package com.ets.gti619.authmodule.features.role;

import com.ets.gti619.authmodule.features.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
public class Role {

    @SequenceGenerator(
            name = "role_sequence",
            sequenceName = "role_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_sequence"
    )
    @Id
    @Column(name = "role_id")
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private Set<User> user;

    public Role(String name) {
        this.name = name;
    }
}
