package com.example.demo;

import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.VideoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class MyController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoService videoService;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        String token = userService.checkUser(username, password);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(token);
    }

    @GetMapping("/register")
    public ResponseEntity<String> register(@RequestParam("username") String username,
                                           @RequestParam("password") String password) {
        if (userService.isUsernameTaken(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userService.generateUser(username, password);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDataBase)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok("Hello, world!");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(
            @RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        if (videoService.generateVideo(file, request)) {
            return ResponseEntity.ok("Success");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
