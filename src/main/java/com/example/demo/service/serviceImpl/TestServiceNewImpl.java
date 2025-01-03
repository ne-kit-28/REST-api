package com.example.demo.service.serviceImpl;

import com.example.demo.domain.User;
import com.example.demo.service.TestService;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service("Second")
@Profile("dev")
public class TestServiceNewImpl implements TestService {
    @Override
    public int deleteUserByHouseNumber(int houseNumber) {
        return 0;
    }

    @Override
    public void saveUser(User user) {
        System.out.println("Yes, I save");
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>();
    }

    @Override
    public Optional<User> loadUserByUsername(String userName) {
        return Optional.of(User.builder()
                .role("ROLE_ADMIN")
                .build());
    }
}
