package com.example.demo.service;

import com.example.demo.models.LikedVideo;
import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import com.example.demo.repo.LikedVideoRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        UserDataBase authorVideo = userRepository.findByUsername(video.getAuthorName());
        authorVideo.increaseCount_likes();
        video.increaseCountLike();
        video.updateRating();
        LikedVideo likedVideo = new LikedVideo();
        likedVideo.setUser(user);
        likedVideo.setVideo(video);
        likedVideoRepository.save(likedVideo);
        return true;
    }

    public void deleteFromLikedVideos(String token, String identifier) {
        Video video = videoRepository.findVideoByIdentifier(identifier);
        UserDataBase user = userRepository.findByToken(token);
        UserDataBase authorVideo = userRepository.findByUsername(video.getAuthorName());
        authorVideo.decreaseCount_likes();
        LikedVideo likedVideo = likedVideoRepository.findByUserAndVideo(user, video);
        video.decreaseCountLike();
        video.updateRating();
        likedVideoRepository.delete(likedVideo);
    }
}
