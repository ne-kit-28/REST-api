package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class TestController {

    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @DeleteMapping("/delete/{house}")
    ResponseEntity<Void> delete(@PathVariable("house") int house) {
        userRepository.deleteByAddress_House(house);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post")
    ResponseEntity<Void> create() {

        List<User> list = Stream.generate(User::generate).limit(10).toList();
        for(User u : list)
            userRepository.save(u);
        return ResponseEntity.ok().build();
    }
}
