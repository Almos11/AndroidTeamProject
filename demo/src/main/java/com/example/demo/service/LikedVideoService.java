package com.example.demo.service;

import com.example.demo.models.LikedVideo;
import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import com.example.demo.repo.LikedVideoRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikedVideoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private LikedVideoRepository likedVideoRepository;
    public boolean addToLikedVideos(String token, String identifier) {
        Video video = videoRepository.findVideoByIdentifier(identifier);
        UserDataBase user = userRepository.findByToken(token);
        LikedVideo existingLikedVideo = likedVideoRepository.findByUserAndVideo(user, video);
        if (existingLikedVideo != null) {
            return false;
        }
        video.increaseCountLike();
        video.updateRating();
        LikedVideo likedVideo = new LikedVideo();
        likedVideo.setUser(user);
        likedVideo.setVideo(video);
        likedVideoRepository.save(likedVideo);
        return true;
    }
}
