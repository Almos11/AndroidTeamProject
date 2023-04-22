package com.example.demo.controller;

import com.example.demo.models.Comment;
import com.example.demo.models.LikedVideo;
import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import com.example.demo.repo.LikedVideoRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import com.example.demo.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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
    private LikedVideoRepository likedVideoRepository;
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
    public ResponseEntity<byte[]> downloadVideo(@RequestParam("Id") String id) {
        byte[] data = videoRepository.findVideoByIdentifier(id).getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("inline").
                filename("video.mp4").build());

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
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
    public ResponseEntity<String> deleteLike(@RequestParam("Id") String id,
                           @RequestHeader("Authorization") String token) {
        Video video = videoRepository.findVideoByIdentifier(id);
        UserDataBase user = userRepository.findByToken(token);
        LikedVideo likedVideo = likedVideoRepository.findByUserAndVideo(user, video);
        if (likedVideo != null) {
            video.decreaseCountLike();

            likedVideoRepository.delete(likedVideo);
           return ResponseEntity.ok("Success");
        }
        return ResponseEntity.badRequest().build();
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
    @PostMapping("/askKoboldAI")
    public ResponseEntity<String> askKoboldAI(@RequestBody ObjectNode objectNode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectNode requestObject = objectMapper.createObjectNode();
        requestObject.put("text", objectNode.get("Hello!").asText());
        HttpEntity<String> requestEntity = new HttpEntity<>(requestObject.toString(), headers);

        String koboldAIUrl = "http://localhost:5001";
        String response = restTemplate.postForObject(koboldAIUrl, requestEntity, String.class);

        return ResponseEntity.ok(response);
    }
}
