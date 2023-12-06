package com.ets.gti619.authmodule.features.clienttype;

import com.ets.gti619.authmodule.features.client.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "types")
@NoArgsConstructor
@Getter
public class ClientType {

    @SequenceGenerator(
            name = "client_type_sequence",
            sequenceName = "client_type_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_type_sequence"
    )
    @Id
    @Column(name = "type_id")
    private long id;

    @Column(name = "type")
    private String type;

    @ManyToMany(mappedBy = "type", fetch = FetchType.EAGER)
    private Set<Client> client;

    public ClientType(String type) {
        this.type = type;
    }
}

