package com.example.demo;

import com.example.demo.models.UserDataBase;
import com.example.demo.repo.UserDataBaseRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class MyController {

    private UserDataBaseRepository userDataBaseRepository;
    private UserService userService;
    static Map<String, User> usersWhoHaveToken;
    static {
        usersWhoHaveToken = new HashMap<>();
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        UserDataBase user = userDataBaseRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody String username, @RequestBody String password) {
        UserDataBase user = new UserDataBase();
        user.setUsername(username);
        user.setPassword(password);
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

}
