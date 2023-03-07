package com.example.demo.service;

import com.example.demo.models.Video;
import com.example.demo.repo.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    private final VideoRepository videoRepository;
    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public void saveVideo(Video video) {
        videoRepository.save(video);
    }
}
