package com.example.springbootcamelkafka.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "contacts")
public class Contact {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "id")
    @Column(nullable = false)
    private Integer contactID;

    @JsonFormat(pattern = "first_name")
    @Column(nullable = false)
    private String firstName;

    @JsonFormat(pattern = "last_name")
    @Column(nullable = false)
    private String lastName;
}
