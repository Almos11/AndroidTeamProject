package com.example.demo.service;

import com.example.demo.models.UserDataBase;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userDataBaseRepository) {
        this.userRepository = userDataBaseRepository;
    }

    public void saveUser(UserDataBase user) {
        userRepository.save(user);
    }
    public boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username) != null;
    }
    public void generateUser(String username, String password) {
        UserDataBase user = new UserDataBase();
        user.setUsername(username);
        user.setPassword(password);
        this.saveUser(user);

    }

    public String checkUser(String username, String password) {
        UserDataBase user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        this.saveUser(user);
        return token;
    }
}
