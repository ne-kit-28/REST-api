package com.example.demo.service.serviceImpl;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("First")
public class TestServiceImpl implements TestService {

    private final UserRepository userRepository;

    @Autowired
    public TestServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public int deleteUserByHouseNumber(int houseNumber) {
        return userRepository.deleteByAddress_House(houseNumber);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public ArrayList<User> getAllUsers() {
        List<User> list = userRepository.findAll();
        return new ArrayList<>(list);
    }

    @Override
    public Optional<User> loadUserByUsername(String userName) {
        return userRepository.findByLogin(userName);
    }
}
