package com.example.demo.controller;

import com.example.demo.domain.Address;
import com.example.demo.domain.User;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegistrationDto;
import com.example.demo.service.TestService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/admin")
public class TestController {

    private final TestService testService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TestController(@Qualifier("First") TestService testService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.testService = testService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.login(),
                            loginDto.password()
                    )
            );
            // Если успешно, устанавливаем аутентификацию в контекст
            //SecurityContextHolder.getContext().setAuthentication(authentication); это не надо так как установка происходит в фильтре

            return ResponseEntity.ok(jwtUtil.generateToken(loginDto.login()));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
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

    @PostMapping("/registration")
    ResponseEntity<String> registration(@RequestBody RegistrationDto registrationDto) {

        User user = User.builder()
                .name(registrationDto.name())
                .surname(registrationDto.surname())
                .login(registrationDto.login())
                .role("ROLE_USER")
                .password(passwordEncoder.encode(registrationDto.password()))
                .address(new Address(registrationDto.street(), registrationDto.houseNumber()))
                .build();

        Optional<User> userOptional = testService.loadUserByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already exist");
        }

        testService.saveUser(user);

        return ResponseEntity.ok("saved successful");
        /*class SaveThread extends Thread {

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
        return ResponseEntity.ok(util.generateToken("login"));*/


    }
}
