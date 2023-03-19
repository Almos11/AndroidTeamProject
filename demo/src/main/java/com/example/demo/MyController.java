package com.example.demo;

import com.example.demo.models.LikedVideo;
import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import com.example.demo.repo.LikedVideoRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.VideoService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private LikedVideoRepository likedVideoRepository;

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
        List<Video> videos = videoRepository.findAllByOrderByRatingDesc();
        List<Double> videoProbabilities = new ArrayList<>();
        double sumRatings = videos.stream().mapToInt(Video::getRating).sum();
        for (Video video : videos) {
            double probability = video.getRating() / sumRatings;
            videoProbabilities.add(probability);
        }
        double randomNumber = Math.random();
        double probabilitySum = 0;
        int videoIndex = 0;
        for (double probability : videoProbabilities) {
            probabilitySum += probability;
            if (randomNumber <= probabilitySum) {
                break;
            }
            videoIndex++;
        }
        Video randomVideo = videos.get(videoIndex);
        String ans = randomVideo.getName();
        return ResponseEntity.ok(ans);
    }

    @GetMapping("/like")
    public String addLike(@RequestParam("Id") String id,
                          @RequestHeader("Authorization") String token) {
        Video video = videoRepository.findVideoByIdentifier(id);
        UserDataBase user = userRepository.findByToken(token);
        LikedVideo existingLikedVideo = likedVideoRepository.findByUserAndVideo(user, video);
        if (existingLikedVideo != null) {
            return "Already liked";
        }
        video.increaseCountLike();
        LikedVideo likedVideo = new LikedVideo();
        likedVideo.setUser(user);
        likedVideo.setVideo(video);
        likedVideoRepository.save(likedVideo);
        return "Success";
    }
    @GetMapping("/dislike")
    public String addDislike(@RequestParam("Id") String id) {
        Video video = videoRepository.findVideoByIdentifier(id);
        video.increaseCountDislike();
        return "Success";
    }
    @GetMapping("/unlike")
    public String deleteLike(@RequestParam("Id") String id,
                           @RequestHeader("Authorization") String token) {
        Video video = videoRepository.findVideoByIdentifier(id);
        UserDataBase user = userRepository.findByToken(token);
        LikedVideo likedVideo = likedVideoRepository.findByUserAndVideo(user, video);
        if (likedVideo != null) {
            video.decreaseCountLike();
            likedVideoRepository.delete(likedVideo);
            return "Success";
        }
        return "Fail";
    }
    @GetMapping("/deleteDislike")
    public void deleteDislike(@RequestParam("Id") String id) {
        Video video = videoRepository.findVideoByIdentifier(id);
        video.decreaseCountDislike();
    }
}
