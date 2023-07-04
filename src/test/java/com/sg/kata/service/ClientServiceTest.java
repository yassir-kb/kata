package com.sg.kata.service;

import com.sg.kata.dto.ClientException;
import com.sg.kata.model.Account;
import com.sg.kata.model.Client;
import com.sg.kata.model.History;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    private ClientService clientService;

    @BeforeEach
    void setUp() throws ClientException {
        clientService = new ClientService();
        List<History> emptyHistory = new ArrayList<>();
        Client client1 = Client.builder()
                .id("1")
                .name("Yassir EL KOBI")
                .status("Prospect")
                .account(Account.builder().iban("123456").balance("1000").build())
                .history(emptyHistory)  // Set an empty list of history
                .build();
        clientService.addProspectClient(client1);
    }

    @Test
    void testGetClient() throws ClientException {
        Client client = clientService.getClient("1");
        assertEquals("1", client.getId());
        assertEquals("Yassir EL KOBI", client.getName());
        assertEquals("Prospect", client.getStatus());
    }

    @Test
    void testGetClient_InvalidId() {
        assertThrows(ClientException.class, () -> clientService.getClient("2"));
    }

    @Test
    void testUpdateClient() throws ClientException {
        Client updatedClient = Client.builder()
                .name("Ludovic PANEL")
                .status("Validated")
                .build();
        clientService.updateClient("1", updatedClient);
        Client client = clientService.getClient("1");
        assertEquals("Ludovic PANEL", client.getName());
        assertEquals("Validated", client.getStatus());
    }

    @Test
    void testAddProspectClient() throws ClientException {
        Client client2 = Client.builder()
                .id("2")
                .name("Ludovic PANEL")
                .status("Prospect")
                .account(Account.builder().iban("789012").balance("2000").build())
                .build();
        clientService.addProspectClient(client2);
        Client client = clientService.getClient("2");
        assertEquals("2", client.getId());
        assertEquals("Ludovic PANEL", client.getName());
        assertEquals("Prospect", client.getStatus());
    }

    @Test
    void testAddProspectClient_InvalidId() {
        Client client2 = Client.builder()
                .name("Ludovic PANEL")
                .status("Prospect")
                .account(Account.builder().iban("789012").balance("2000").build())
                .build();
        assertThrows(ClientException.class, () -> clientService.addProspectClient(client2));
    }

    @Test
    void testValidateClient() throws ClientException {
        clientService.validateClient("1");
        Client client = clientService.getClient("1");
        assertEquals("Validated", client.getStatus());
    }

    @Test
    void testDeleteClient() throws ClientException {
        clientService.deleteClient("1");
        assertThrows(ClientException.class, () -> clientService.getClient("1"));
    }

    @Test
    void testGetAccount() throws ClientException {
        Account account = clientService.getAccount("1", "123456");
        assertEquals("123456", account.getIban());
        assertEquals("1000", account.getBalance());
    }

    @Test
    void testGetAccount_InvalidIban() {
        assertThrows(ClientException.class, () -> clientService.getAccount("1", "789012"));
    }

    @Test
    void testCredit() throws ClientException {
        clientService.credit("1", "123456", "500");
        Account account = clientService.getAccount("1", "123456");
        assertEquals("1500.0", account.getBalance());
        List<History> history = clientService.getHistory("1");
        assertEquals(1, history.size());
        History operation = history.get(0);
        assertEquals("1", operation.getId());
        assertEquals("123456", operation.getIban());
        assertEquals("Credit", operation.getOperation());
        assertEquals("500", operation.getAmount());
    }

    @Test
    void testGetHistory() throws ClientException {
        List<History> history = clientService.getHistory("1");
        assertNotNull(history);
        assertEquals(0, history.size());
    }

    @Test
    void testGetOperation() throws ClientException {
        clientService.credit("1", "123456", "500");
        History operation = clientService.getOperation("1", "1");
        assertEquals("1", operation.getId());
        assertEquals("123456", operation.getIban());
        assertEquals("Credit", operation.getOperation());
        assertEquals("500", operation.getAmount());
    }

    @Test
    void testGetOperation_InvalidOpId() {
        assertThrows(ClientException.class, () -> clientService.getOperation("1", "2"));
    }
}
