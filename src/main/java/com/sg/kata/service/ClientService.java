package com.sg.kata.service;

import com.sg.kata.dto.ClientException;
import com.sg.kata.model.Account;
import com.sg.kata.model.Client;

import java.util.HashMap;
import java.util.Map;

public class ClientService {

    private final Map<String, Client> clients;

    public ClientService() {
        this.clients = new HashMap<>();
    }

    public Client getClient(String id) throws ClientException {
        Client client = findClientById(id);
        if (client == null) {
            throw new ClientException("Client not found");
        }
        return client;
    }

    public void updateClient(String id, Client updatedClient) throws ClientException {
        Client client = getClient(id);
        client.setName(updatedClient.getName());
        client.setStatus(updatedClient.getStatus());
    }

    public void addProspectClient(Client client) throws ClientException {
        if (client.getId() == null || client.getId().isEmpty()) {
            throw new ClientException("Client ID is required");
        }
        if (clients.containsKey(client.getId())) {
            throw new ClientException("Client ID already exists");
        }
        clients.put(client.getId(), client);
    }

    public void validateClient(String id) throws ClientException {
        Client client = getClient(id);
        client.setStatus("Validated");
    }

    public void deleteClient(String id) throws ClientException {
        if (clients.remove(id) == null) {
            throw new ClientException("Client not found");
        }
    }

    public Account getAccount(String clientId, String iban) throws ClientException {
        Client client = getClient(clientId);
        Account account = client.getAccount();
        if (account.getIban().equals(iban)) {
            return account;
        } else {
            throw new ClientException("Invalid IBAN");
        }
    }

    private Client findClientById(String id) {
        for (Client client : clients.values()) {
            if (client.getId().equals(id)) {
                return client;
            }
        }
        return null;
    }
}
