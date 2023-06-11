package com.example.demo.service;

import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import com.example.demo.models.VideoData;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoDataService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public String setupJsonFormatVideoData(String token) throws JsonProcessingException {
        UserDataBase userDataBase = userRepository.findByToken(token);
        String id = videoRepository.getTopRatedVideoId(userDataBase.getId());
        Video video = videoRepository.findVideoByIdentifier(id);
        VideoData videoData = new VideoData();
        videoData.setId(id);
        videoData.setAuthor_name(video.getAuthorName());
        videoData.setLikes(video.getCountLike());
        videoData.setRating(video.getRating());
        videoData.setComments(video.getCountComments());
        videoData.setViews(video.getViews());
        videoData.setAuthor_id(userDataBase.getId().intValue());
        return objectMapper.writeValueAsString(videoData);
    }
}
