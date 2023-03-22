package com.example.demo.service;

import com.example.demo.models.DislikedVideo;
import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import com.example.demo.repo.DislikedVideoRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DislikedVideoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private DislikedVideoRepository dislikedVideoRepository;

    public boolean addToDislikedVideos(String token, String identifier) {
        Video video = videoRepository.findVideoByIdentifier(identifier);
        UserDataBase user = userRepository.findByToken(token);
        DislikedVideo existingDislikedVideo = dislikedVideoRepository.findByUserAndVideo(user, video);
        if (existingDislikedVideo != null) {
            return false;
        }
        video.increaseCountDislike();
        video.updateRating();
        DislikedVideo dislikedVideo = new DislikedVideo();
        dislikedVideo.setUser(user);
        dislikedVideo.setVideo(video);
        dislikedVideoRepository.save(dislikedVideo);
        return true;
    }
}
