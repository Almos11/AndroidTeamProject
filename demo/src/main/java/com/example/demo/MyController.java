package com.example.demo;

import com.example.demo.models.UserDataBase;
import com.example.demo.repo.UserDataBaseRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class MyController {

    @Autowired
    private UserDataBaseRepository userDataBaseRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        UserDataBase user = userDataBaseRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/register")
    public ResponseEntity<String> register(@RequestParam("username") String username,
                                           @RequestParam("password") String password) {
        if (userDataBaseRepository.findByUsername(username) != null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDataBase user = new UserDataBase();
        user.setUsername(username);
        user.setPassword(password);
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

}
