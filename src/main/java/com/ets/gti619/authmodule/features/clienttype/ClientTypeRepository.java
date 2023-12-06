package com.ets.gti619.authmodule.features.clienttype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientTypeRepository extends JpaRepository<ClientType, Long> {
    Optional<ClientType> findByType(String type);
}
