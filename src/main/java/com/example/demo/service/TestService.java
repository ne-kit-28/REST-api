package com.example.demo.service;

import com.example.demo.domain.User;

import java.util.ArrayList;
import java.util.Optional;

public interface TestService {
    int deleteUserByHouseNumber(int houseNumber);

    void saveUser(User user);

    ArrayList<User> getAllUsers();

    Optional<User> loadUserByUsername(String userName);
}
