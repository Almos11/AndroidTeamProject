package com.example.demo.service;

import com.example.demo.models.UserDataBase;
import com.example.demo.repo.UserRepository;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username) != null;
    }
    public void generateUser(String username, String password) {
        UserDataBase user = new UserDataBase();
        user.setUsername(username);
        user.setPassword(password);
        user.setCount_likes(0);
        user.setCount_videos(0);
        user.setDescription("");
        userRepository.save(user);

    }

    public String checkUser(String username, String password) {
        UserDataBase user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userRepository.save(user);
        return token;
    }

    public boolean addNewUser(ObjectNode objectNode) {
        String username = objectNode.get("username").asText();
        String password = objectNode.get("password").asText();
        if (this.isUsernameTaken(username)) {
            return false;
        }
        this.generateUser(username, password);
        return true;
    }
}
