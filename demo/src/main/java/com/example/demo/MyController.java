package com.example.demo;

import com.example.demo.models.UserDataBase;
import com.example.demo.repo.VideoRepository;
import com.example.demo.service.DislikedVideoService;
import com.example.demo.service.LikedVideoService;
import com.example.demo.service.UserService;
import com.example.demo.service.VideoService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
public class MyController {
    @Autowired
    private UserService userService;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoService videoService;
    @Autowired
    private LikedVideoService likedVideoService;
    @Autowired
    private DislikedVideoService dislikedVideoService;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        String token = userService.checkUser(username, password);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody ObjectNode objectNode) {
        String username = objectNode.get("name").asText();
        String password = objectNode.get("pass").asText();
        if (userService.isUsernameTaken(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
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
        return ResponseEntity.ok("Hello, world");
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token) throws IOException, NoSuchAlgorithmException {
        String id = videoService.generateVideo(file, token);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(id);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadVideo(@RequestParam("Id") String id,
                                                @RequestHeader("Authorization") String token) {
        byte[] data = videoRepository.findVideoByIdentifier(id).getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("inline").
                filename("video.mp4").build());

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    @GetMapping("/sortVideo")
    public ResponseEntity<String> getTopVideo() {
        videoRepository.updateVideos();
        return ResponseEntity.ok("success");
    }

    @GetMapping("/like")
    public String addLike(@RequestParam("Id") String id,
                          @RequestHeader("Authorization") String token) {
        if (likedVideoService.addToLikedVideos(token, id)) {
            return "Success";
        } else {
            return "Already exist";
        }
    }
    @GetMapping("/dislike")
    public String addDislike(@RequestParam("Id") String id,
                             @RequestHeader("Authorization") String token) {
        if (dislikedVideoService.addToDislikedVideos(token, id)) {
            return "Success";
        } else {
            return "Already exist";
        }
    }

    /*@GetMapping("/unlike")
    public String deleteLike(@RequestParam("Id") String id,
                           @RequestHeader("Authorization") String token) {
        Video video = videoRepository.findVideoByIdentifier(id);
        UserDataBase user = userRepository.findByToken(token);
        LikedVideo likedVideo = likedVideoRepository.findByUserAndVideo(user, video);
        if (likedVideo != null) {
            video.decreaseCountLike();
            video.updateRating();
            likedVideoRepository.delete(likedVideo);
            return "Success";
        }
        return "Fail";
    }
    @GetMapping("/deleteDislike")
    public void deleteDislike(@RequestParam("Id") String id) {
        Video video = videoRepository.findVideoByIdentifier(id);
        video.decreaseCountDislike();
        video.updateRating();
    }*/
}
