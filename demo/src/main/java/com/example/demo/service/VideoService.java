package com.example.demo.service;

import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public String generateVideo(MultipartFile file, String token) throws IOException, NoSuchAlgorithmException {
        if (file.getSize() < maxFileSizeInBytes) {
            UserDataBase user = userRepository.findByToken(token);
            if (user == null) {
                return null;
            }
            String videoName = file.getOriginalFilename();
            Video video = new Video();
            byte[] videoData = file.getBytes();
            String identifier = computeMD5(videoData);
            if (videoRepository.findVideoByIdentifier(identifier) != null) {
                return null;
            }
            video.setIdentifier(identifier);
            video.setName(videoName);
            video.setData(videoData);
            video.setUser(user);
            this.saveVideo(video);
            return video.getIdentifier();
        }
        return null;
    }
    public static String computeMD5(byte[] fileBytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(fileBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

}
