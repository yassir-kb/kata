package com.sg.kata.service;

import com.sg.kata.dto.ClientException;
import com.sg.kata.model.Account;
import com.sg.kata.model.Client;
import com.sg.kata.model.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    private List<Client> clients = new ArrayList<>();

    public Client getClient(String id) throws ClientException {
        for (Client client : clients) {
            if (client.getId().equals(id)) {
                return client;
            }
        }
        throw new ClientException("Client not found");
    }

    public void updateClient(String id, Client body) throws ClientException {
        Client existingClient = getClient(id);
        existingClient.setName(body.getName());
        existingClient.setStatus(body.getStatus());
    }

    public void addProspectClient(Client client) throws ClientException {
        if (client.getId() == null || client.getId().isEmpty()) {
            throw new ClientException("Client ID must be provided");
        }
        clients.add(client);
    }

    public void validateClient(String id) throws ClientException {
        Client client = getClient(id);
        client.setStatus("Validated");
    }

    public void deleteClient(String id) throws ClientException {
        Client client = getClient(id);
        clients.remove(client);
    }

    public Account getAccount(String id, String iban) throws ClientException {
        Client client = getClient(id);
        Account account = client.getAccount();
        if (account.getIban().equals(iban)) {
            return account;
        } else {
            throw new ClientException("Account not found");
        }
    }

    public void credit(String id, String iban, String amount) throws ClientException {
        Account account = getAccount(id, iban);
        double currentBalance = Double.parseDouble(account.getBalance());
        double creditAmount = Double.parseDouble(amount);
        double newBalance = currentBalance + creditAmount;
        account.setBalance(String.valueOf(newBalance));
        addHistory(id, iban, "Credit", amount);
    }

    public List<History> getHistory(String id) throws ClientException {
        Client client = getClient(id);
        return client.getHistory();
    }

    public History getOperation(String clientId, String operationId) throws ClientException {
        Client client = getClient(clientId);
        List<History> history = client.getHistory();
        for (History operation : history) {
            if (operation.getId().equals(operationId)) {
                return operation;
            }
        }
        throw new ClientException("Operation not found");
    }


    private void addHistory(String id, String iban, String operation, String amount) throws ClientException {
        Client client = getClient(id);
        List<History> history = client.getHistory();
        String date = LocalDateTime.now().toString();
        History newOperation = History.builder()
                .id(String.valueOf(history.size() + 1))
                .iban(iban)
                .operation(operation)
                .amount(amount)
                .date(date)
                .build();
        history.add(newOperation);
    }

}
