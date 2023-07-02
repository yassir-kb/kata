package com.sg.kata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sg.kata.model.Account;
import com.sg.kata.model.Client;
import com.sg.kata.model.History;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientResponse {
    private Object data;
    private String message;

    public ClientResponse(Object data, String message) {
        this.data = data;
        this.message = message;
    }
}
