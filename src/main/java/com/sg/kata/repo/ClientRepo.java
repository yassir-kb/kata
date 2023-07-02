package com.sg.kata.repo;

import com.sg.kata.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClientRepo extends MongoRepository<Client, String> {
    Optional<Client> findById(String id);
}
