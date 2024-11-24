package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/admin")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("get/all")
    ResponseEntity<ArrayList<User>> getAll() {
        return ResponseEntity.ok(testService.getAllUsers());
    }

    @DeleteMapping("/delete/{house}")
    ResponseEntity<Integer> delete(@PathVariable("house") int house) {
        int count = testService.deleteUserByHouseNumber(house);
        return ResponseEntity.ok().body(count);
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

                //this.setDaemon(true); //установка демона

                //Thread.interrupted(); //возвращает прерван ли поток

                testService.saveUser(user);
                System.out.println("saved by " + this.getName());

                this.interrupt(); //тут ничего не делает, но переводит бул поле в true
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
