package com.sg.kata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    @Id
    private String id;
    private String iban;
    private String operation;
    private String amount;
    private String date;
}
