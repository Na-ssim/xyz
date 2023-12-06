package com.ets.gti619.authmodule.configuration;

import com.ets.gti619.authmodule.features.client.Client;
import com.ets.gti619.authmodule.features.client.ClientService;
import com.ets.gti619.authmodule.features.clienttype.ClientType;
import com.ets.gti619.authmodule.features.clienttype.ClientTypeService;
import com.ets.gti619.authmodule.features.clienttype.ClientTypes;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class ClientConfiguration {

    private final Client clientRes1;
    private final Client clientRes2;
    private final Client clientRes3;
    private final Client clientRes4;
    private final Client clientRes5;

    private final ClientType clientsTypeResidentiels;
    private final ClientType clientsTypeAffaire;

    public ClientConfiguration() {


        clientsTypeResidentiels = new ClientType(ClientTypes.CLIENTS_RESIDENTIELS.getType());
        clientsTypeAffaire = new ClientType(ClientTypes.CLIENTS_AFFAIRE.getType());

        clientRes1 = new Client("TestClient1", "address1","telephone1", Collections.singleton(clientsTypeResidentiels));
        clientRes2 = new Client("TestClient2", "address2","telephone2", Collections.singleton(clientsTypeResidentiels));
        clientRes3 = new Client("TestClient3", "address3","telephone3", Collections.singleton(clientsTypeResidentiels));
        clientRes4 = new Client("TestClient4", "address4","telephone4", Collections.singleton(clientsTypeAffaire));
        clientRes5 = new Client("TestClient5", "address5","telephone5", Collections.singleton(clientsTypeAffaire));
    }

    /**
     * A configuration bean to create fictitious clients
     * @param clientService Allows the creation of clients
     * @param clientTypeService Allows the creation of different client types
     * @return A command line runner object executing the creation of clients
     */
    @Bean
    CommandLineRunner clientConfig(ClientService clientService, ClientTypeService clientTypeService) {
        return args -> {
            if (!clientTypeService.clientTypeExists(clientsTypeResidentiels.getType())) {
                clientTypeService.addClientType(clientsTypeResidentiels);
            }
            if (!clientTypeService.clientTypeExists(clientsTypeAffaire.getType())) {
                clientTypeService.addClientType(clientsTypeAffaire);
            }

            if (!clientService.clientExists(clientRes1.getClientname())) {
                clientService.addClient(clientRes1);
            }
            if (!clientService.clientExists(clientRes2.getClientname())) {
                clientService.addClient(clientRes2);
            }
            if (!clientService.clientExists(clientRes3.getClientname())) {
                clientService.addClient(clientRes3);
            }
            if (!clientService.clientExists(clientRes4.getClientname())) {
                clientService.addClient(clientRes4);
            }
            if (!clientService.clientExists(clientRes5.getClientname())) {
                clientService.addClient(clientRes5);
            }

        };
    }
}
