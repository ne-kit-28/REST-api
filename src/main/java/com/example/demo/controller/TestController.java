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

        class SaveThread extends Thread {

            private final User user;

            public SaveThread(int id, User user) {
                super(String.valueOf(id));
                this.user = user;
            }

            @Override
            public void run() {
                userRepository.save(user);
                System.out.println("saved by " + this.getName());
            }
        }

        List<User> list = Stream.generate(User::generate).limit(1000).toList();
        int i = 1;
        for(User u : list) {
            new SaveThread(i, u).start();
            ++i;
            //userRepository.save(u);
        }
        return ResponseEntity.ok().build();
    }
}
