package com.sg.kata.controller;

import com.sg.kata.dto.ClientException;
import com.sg.kata.dto.ClientResponse;
import com.sg.kata.model.Account;
import com.sg.kata.model.Client;
import com.sg.kata.model.History;
import com.sg.kata.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService service;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable String id) {
        try {
            Client client = service.getClient(id);
            return new ResponseEntity<>(new ClientResponse(client, null), HttpStatus.OK);
        } catch (ClientException ce) {
            return new ResponseEntity<>(new ClientResponse(null, ce.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable String id, @RequestBody Client body) {
        try {
            service.updateClient(id, body);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ClientException ce) {
            return new ResponseEntity<>(new ClientResponse(null, ce.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ClientResponse> addProspectClient(@RequestBody Client client) {
        try {
            service.addProspectClient(client);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ClientException ex) {
            return new ResponseEntity<>(new ClientResponse(null, ex.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> validateClient(@PathVariable String id) {
        try {
            service.validateClient(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ClientException ex) {
            return new ResponseEntity<>(new ClientResponse(null, ex.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ClientResponse> deleteClient(@PathVariable String id) {
        try {
            service.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ClientException ce) {
            return new ResponseEntity<>(new ClientResponse(null, ce.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/account/{iban}")
    public ResponseEntity<ClientResponse> getAccount(@PathVariable String id, @PathVariable String iban) {
        try {
            Account account = service.getAccount(id, iban);
            return new ResponseEntity<>(new ClientResponse(account, null), HttpStatus.OK);
        } catch (ClientException ce) {
            return new ResponseEntity<>(new ClientResponse(null, ce.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}/account/{iban}")
    public ResponseEntity<ClientResponse> credit(@PathVariable String id, @PathVariable String iban, @RequestBody String amount) {
        try {
            service.credit(id, iban, amount);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ClientException ex) {
            return new ResponseEntity<>(new ClientResponse(null, ex.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/history")
    public ResponseEntity<ClientResponse> getAccount(@PathVariable String id) {
        try {
            List<History> history = service.getHistory(id);
            return new ResponseEntity<>(new ClientResponse(history, null), HttpStatus.OK);
        } catch (ClientException ce) {
            return new ResponseEntity<>(new ClientResponse(null, ce.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/history/{opId}")
    public ResponseEntity<ClientResponse> getOperation(@PathVariable String id, @PathVariable String opId) {
        try {
            History history = service.getOperation(id, opId);
            return new ResponseEntity<>(new ClientResponse(history, null), HttpStatus.OK);
        } catch (ClientException ce) {
            return new ResponseEntity<>(new ClientResponse(null, ce.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
