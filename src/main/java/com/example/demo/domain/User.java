package com.example.demo.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Random;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @UuidGenerator
    @Id
    private UUID id;

    private String name;

    private String surname;

    @Embedded
    private Address address;

    public static User generate() {
        return User.builder()
                .name("Ivan" + (int)(Math.random() * 10))
                .surname("Ivanov")
                .address(new Address("Mira", (int)(Math.random() * 100)))
                .build();
    }
}
