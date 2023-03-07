package com.example.demo.service;

import com.example.demo.models.UserDataBase;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
