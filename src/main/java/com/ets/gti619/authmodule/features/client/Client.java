package com.ets.gti619.authmodule.features.client;

import com.ets.gti619.authmodule.features.clienttype.ClientType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
public class Client {


    @Id
    @SequenceGenerator(
            name = "client_sequence",
            sequenceName = "client_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_sequence"
    )
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "clientname")
    private String clientname;

    @Column(name = "telephone")
    private String telephone;


    @Column(name = "addresse")
    private String addresse;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "client_type",
            joinColumns = @JoinColumn(name = "type_id_client"),
            inverseJoinColumns = @JoinColumn(name = "type_id"))
    private Set<ClientType> type;


    public Client(String clientname,String addresse, String telephone, Set<ClientType> type) {
        this.clientname = clientname;
        this.telephone = telephone;
        this.addresse = addresse;
        this.type = type;
    }
}
