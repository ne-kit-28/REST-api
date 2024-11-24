package com.example.demo.service;

import com.example.demo.domain.User;

import java.util.ArrayList;

public interface TestService {
    int deleteUserByHouseNumber(int houseNumber);

    void saveUser(User user);

    ArrayList<User> getAllUsers();
}
