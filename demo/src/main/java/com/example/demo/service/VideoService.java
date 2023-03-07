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
    private final VideoRepository videoRepository;
    @Autowired
    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public void saveVideo(Video video) {
        videoRepository.save(video);
    }

    public boolean generateVideo(MultipartFile file, HttpServletRequest request) throws IOException  {
        if (file.getSize() < maxFileSizeInBytes) {
            String token = request.getParameter("token");
            UserDataBase user = userRepository.findByToken(token);
            Video video = new Video();
            video.setName(file.getOriginalFilename());
            video.setData(file.getBytes());
            video.setUser(user);
            this.saveVideo(video);
            return true;
        }
        return false;
    }

}
