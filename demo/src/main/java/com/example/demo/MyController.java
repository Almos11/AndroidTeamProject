package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class MyController {
    public static Map<String, String> list_users;
    static {
        list_users = new HashMap<>();
        list_users.put("user1", "password1");
        list_users.put("user2", "password2");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if (!list_users.containsKey(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!(list_users.get(username).equals(password))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String randomString = UUID.randomUUID().toString();
        return ResponseEntity.ok(randomString);
    }
}
