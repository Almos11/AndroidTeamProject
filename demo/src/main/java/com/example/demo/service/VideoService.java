package com.example.demo.service;

import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class VideoService {

    //100 Мбайт
    static long maxFileSizeInBytes = 104857600;

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  VideoRepository videoRepository;
    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public void saveVideo(Video video) {
        videoRepository.save(video);
    }

    public Long generateVideo(MultipartFile file, String token) throws IOException  {
        if (file.getSize() < maxFileSizeInBytes) {
            UserDataBase user = userRepository.findByToken(token);
            if (user == null) {
                return -1L;
            }
            String videoName = file.getOriginalFilename();
            Video video = new Video();
            byte[] videoData = file.getBytes();
            video.setName(videoName);
            video.setData(videoData);
            video.setUser(user);
            this.saveVideo(video);
            return video.getId();
        }
        return -1L;
    }

}
