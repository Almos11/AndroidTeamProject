package com.example.demo.controller;

import com.example.demo.models.*;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import com.example.demo.repo.WordRepository;
import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
    private UserRepository userRepository;
    @Autowired
    private ViewService viewService;
    @Autowired
    private VideoDataService videoDataService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private WordService wordService;
    @Autowired
    private UserDataService userDataService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody ObjectNode objectNode) {
        String username = objectNode.get("username").asText();
        String password = objectNode.get("password").asText();
        String token = userService.checkUser(username, password);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody ObjectNode objectNode) {
        if (userService.addNewUser(objectNode)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
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
    public ResponseEntity<byte[]> downloadVideo(@RequestParam("Id") String id) {
        byte[] data = videoRepository.findVideoByIdentifier(id).getData();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/like")
    public ResponseEntity<Void> addLike(@RequestParam("Id") String id,
                          @RequestHeader("Authorization") String token) {
        if (likedVideoService.addToLikedVideos(token, id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/unlike")
    public ResponseEntity<Void> deleteLike(@RequestParam("Id") String id,
                           @RequestHeader("Authorization") String token) {
        likedVideoService.deleteFromLikedVideos(token, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/watch")
    public ResponseEntity<Void> markVideoAsWatched(@RequestParam("Identifier") String video_identifier,
                                                   @RequestHeader("Authorization") String token) {
        viewService.addView(token, video_identifier);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/getComments")
    public  ResponseEntity<String> getComments(
            @RequestParam("Identifier") String video_identifier) throws JsonProcessingException {
        Video video = videoRepository.findVideoByIdentifier(video_identifier);
        if (video == null) {
            return ResponseEntity.notFound().build();
        }
        List<Comment> comments = video.getComments();
        String jsonData = objectMapper.writeValueAsString(comments);
        return ResponseEntity.ok(jsonData);
    }

    @GetMapping("/addComment")
    public ResponseEntity<Void> addComment(@RequestParam("Identifier") String video_identifier,
                                           @RequestParam("Content") String content,
                                           @RequestHeader("Authorization") String token) {
        commentService.addComment(video_identifier, token, content);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("/words")
    public ResponseEntity<String> addWords(@RequestBody List<String> words) {
        wordService.saveWords(words);
        return ResponseEntity.ok("Words added successfully.");
    }

    @GetMapping("/nextVideo")
    @ResponseBody
    public ResponseEntity<String> getTopVideo(@RequestHeader("Authorization") String token)
            throws JsonProcessingException {
        String jsonData = videoDataService.setupJsonFormatVideoData(token);
        if (jsonData == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(jsonData);
    }
    @GetMapping("/getUserInfo")
    @ResponseBody
    public ResponseEntity<String> getUserInfo(@RequestParam("id") Long id)
            throws JsonProcessingException {
        String info = userDataService.setupJsonFormatUserData(id);
        return ResponseEntity.ok((info));
    }
    @GetMapping("/description")
    public void addDescription(@RequestParam("description") String description,
                                               @RequestHeader("authorization") String token) {
        UserDataBase userDataBase = userRepository.findByToken(token);
        userDataBase.setDescription(description);
        userRepository.save(userDataBase);
    }
}
