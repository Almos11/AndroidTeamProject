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
    public static Map<String, String> listUsers;
    static {
        listUsers = new HashMap<>();
        listUsers.put("user1", "password1");
        listUsers.put("user2", "password2");
    }
    static Map<String, User> listUsersWhoHaveToken;
    static {
        listUsersWhoHaveToken = new HashMap<>();
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        if (!listUsers.containsKey(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!(listUsers.get(username).equals(password))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token;
        if (listUsersWhoHaveToken.containsKey(username)) {
            token = listUsersWhoHaveToken.get(username).getToken();
        } else {
            token = UUID.randomUUID().toString();
            User user = new User(username, token);
            listUsersWhoHaveToken.put(username, user);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,
                    null, new ArrayList<>()));
        }
        return ResponseEntity.ok(token);
    }

}
