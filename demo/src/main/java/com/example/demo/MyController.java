package com.example.demo;

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
    public static Map<String, String> users = new HashMap<>();
    /*static {
        users = new HashMap<>();
        users.put("user1", "password1");
        users.put("user2", "password2");
    }*/
    static Map<String, User> usersWhoHaveToken;
    static {
        usersWhoHaveToken = new HashMap<>();
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if (!users.containsKey(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!(users.get(username).equals(password))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token;
        if (usersWhoHaveToken.containsKey(username)) {
            token = usersWhoHaveToken.get(username).getToken();
        } else {
            token = UUID.randomUUID().toString();
            User user = new User(username, token);
            usersWhoHaveToken.put(username, user);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,
                    null, new ArrayList<>()));
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password) {
        if (users.containsKey(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }
        users.put(username, password);
        return ResponseEntity.ok("User registered successfully");
    }

}
