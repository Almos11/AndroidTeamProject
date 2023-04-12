package com.example.demo.service;

import com.example.demo.models.Comment;
import com.example.demo.models.Video;
import com.example.demo.repo.CommentRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserRepository userRepository;

    public void addComment(String video_identifier, String token, String content) {
        Video video = videoRepository.findVideoByIdentifier(video_identifier);
        Comment comment = new Comment();
        comment.setVideo(video);
        comment.setContent(content);
        comment.setAuthor(userRepository.findByToken(token).getUsername());
        comment.setCreationTime(new Date());
        commentRepository.save(comment);
        video.increaseCountComments();
    }
}