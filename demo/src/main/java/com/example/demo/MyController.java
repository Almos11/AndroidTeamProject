package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
        // Проверяем, есть ли пользователь в Map-е
        if (!list_users.containsKey(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The username or password you entered is incorrect");
        }

        // Проверяем, соответствует ли пароль
        if (!(list_users.get(username).equals(password))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The username or password you entered is incorrect");
        }

        // Авторизация прошла успешно
        return ResponseEntity.ok("Authorization was successful");
    }
}
