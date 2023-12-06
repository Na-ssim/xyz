package com.ets.gti619.authmodule.features.client;

import com.ets.gti619.authmodule.features.clienttype.ClientType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepository clientRepository;

    public void addClient(Client client) {
        clientRepository.save(client);
    }


    public boolean clientExists(String clientName) {
        return clientRepository.findByClientname(clientName).isPresent();
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<Client> getClientsByType(ClientType type) {
        return clientRepository.findAllByType(type);
    }

    public Optional<Client> findClientById(long id) {
        return clientRepository.findById(id);
    }

}
