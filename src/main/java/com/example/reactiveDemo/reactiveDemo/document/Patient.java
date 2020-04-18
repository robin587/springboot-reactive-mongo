package com.example.reactiveDemo.reactiveDemo.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
public class Patient {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String address;

}
