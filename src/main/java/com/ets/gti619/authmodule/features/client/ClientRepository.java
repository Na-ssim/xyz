package com.ets.gti619.authmodule.features.client;

import com.ets.gti619.authmodule.features.clienttype.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional findByClientname(String clientName);
    List<Client> findAllByType(ClientType type);
}
