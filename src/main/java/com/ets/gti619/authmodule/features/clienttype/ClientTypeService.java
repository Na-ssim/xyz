package com.ets.gti619.authmodule.features.clienttype;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientTypeService {

    private ClientTypeRepository clientTypeRepository;

    public void addClientType(ClientType client) {
        clientTypeRepository.save(client);
    }

    public boolean clientTypeExists(String typeName) {
        return clientTypeRepository.findByType(typeName).isPresent();
    }

    public Optional<ClientType> getTypeByName(String name) {
        return clientTypeRepository.findByType(name);
    }

    public List<ClientType> getAllClientTypes() {
        return clientTypeRepository.findAll();
    }

    public Optional<ClientType> findClientTypeById(long id) {
        return clientTypeRepository.findById(id);
    }
}
