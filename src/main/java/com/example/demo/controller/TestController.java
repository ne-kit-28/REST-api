package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.service.TestService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/admin")
public class TestController {

    private final TestService testService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public TestController(@Qualifier("Second") TestService testService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.testService = testService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public String login() {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("login", "password")
        );
        return jwtUtil.generateToken(auth.getName());
    }

    @GetMapping("get/all")
    //@PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ArrayList<User>> getAll() {
        return ResponseEntity.ok(testService.getAllUsers());
    }

    @DeleteMapping("/delete/{house}")
    ResponseEntity<Integer> delete(@PathVariable("house") int house) {
        int count = testService.deleteUserByHouseNumber(house);
        return ResponseEntity.ok().body(count);
    }

    @PostMapping("/post")
    ResponseEntity<String> create() {

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

        List<User> list = Stream.generate(User::generate).limit(1).toList();
        int i = 1;
        for(User u : list) {
            new SaveThread(i, u).start();
            ++i;
            //userRepository.save(u);
        }

        JwtUtil util = new JwtUtil();
        return ResponseEntity.ok(util.generateToken("login"));
    }
}
