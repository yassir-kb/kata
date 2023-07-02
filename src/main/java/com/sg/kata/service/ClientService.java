package com.sg.kata.service;

import com.sg.kata.dto.ClientException;
import com.sg.kata.model.Account;
import com.sg.kata.model.Client;
import com.sg.kata.model.History;
import com.sg.kata.repo.ClientRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.lang.Double.parseDouble;

@Service
public class ClientService {
    @Autowired
    private ClientRepo clientRepo;

    public List<Client> getAllClients() {
        return clientRepo.findAll();
    }

    public Client getClient(String id) throws ClientException {
        Optional<Client> clientOptional = clientRepo.findById(id);
        if (clientOptional.isPresent()) {
            return clientOptional.get();
        } else {
            throw new ClientException("Client does not exist!");
        }
    }

    public void updateClient(String id, Client body) throws ClientException {
        Optional<Client> clientOptional = clientRepo.findById(id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            BeanUtils.copyProperties(body, client);
            clientRepo.save(client);
        } else {
            throw new ClientException("Client cannot be updated!");
        }
    }


    public void addProspectClient(Client client) throws ClientException {
        for (int i = 0; i < clientRepo.findAll().size(); i++) {
            if (clientRepo.findAll().get(i).getName().equals(client.getName())) {
                throw new ClientException("Client incorrect or already added !");
            } else {
                break;
            }
        }
        client.setStatus("Prospect");
        clientRepo.insert(client);
    }

    public void validateClient(String id) throws ClientException {
        Client client = getClient(id);
        if (client == null) {
            throw new ClientException("Client does not exist !");
        } else {
            Account account = new Account();
            account.setBalance("0");

            client.setStatus("Validated");
            client.setAccount(account);

            clientRepo.save(client);
        }
    }

    public void deleteClient(String id) throws ClientException {
        Client client = getClient(id);
        if (client == null) {
            throw new ClientException("Client does not exist !");
        } else {
            clientRepo.deleteById(id);
        }
    }


    public Account getAccount(String id, String iban) throws ClientException {
        Client client = getClient(id);
        if (client == null) {
            throw new ClientException("Client does not exist !");
        } else if (!client.getAccount().getIban().equals(iban)) {
            throw new ClientException("Account does not exist or Client is Prospect!");
        } else {
            return client.getAccount();
        }
    }

    public void credit(String id, String iban, String amount) throws ClientException {
        Client client = getClient(id);
        Double famount = parseDouble(amount);
        if (client == null) {
            throw new ClientException("Client does not exist !");
        } else if (!client.getAccount().getIban().equals(iban)) {
            throw new ClientException("Account does not exist or Client is Prospect!");
        } else if (famount == null) {
            throw new ClientException("Amount is not a number!");
        } else {
            Double balance = parseDouble(client.getAccount().getBalance());
            client.getAccount().setBalance(String.valueOf(balance + famount));

            History history = new History();
            history.setIban(client.getAccount().getIban());
            history.setAmount(amount);
            history.setDate(String.valueOf(LocalDateTime.now()));
            if (famount > 0) {
                history.setOperation("Deposit");
            } else {
                history.setOperation("Withdraw");
            }
            client.getHistory().add(history);
        }
    }

    public List<History> getHistory(String id) throws ClientException {
        Client client = getClient(id);
        if (client == null) {
            throw new ClientException("Client does not exist !");
        } else {
            return client.getHistory();
        }
    }

    public History getOperation(String id, String opId) throws ClientException {
        Client client = getClient(id);
        History history = new History();
        for (int i = 0; i < client.getHistory().size(); i++) {
            if (client.getHistory().get(i).getIban().equals(opId)) {
                history = client.getHistory().get(i);
            }
        }
        if (client == null) {
            throw new ClientException("Client does not exist !");
        } else if (history == null) {
            throw new ClientException("Operation does not exist !");
        } else {
            return history;
        }
    }
}
