package com.example.demo.dto;

public record RegistrationDto(
        String login,
        String password,
        String name,
        String surname,
        String street,
        int houseNumber
){}
