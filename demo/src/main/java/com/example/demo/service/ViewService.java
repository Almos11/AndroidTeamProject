package com.example.demo.service;

import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import com.example.demo.models.View;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.VideoRepository;
import com.example.demo.repo.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ViewService {

    @Autowired
    private ViewRepository viewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;

    public void addView(String userToken, String videoId) {
        View view = new View();
        UserDataBase user = userRepository.findByToken(userToken);
        Video video = videoRepository.findVideoByIdentifier(videoId);
        video.increaseCountViews();
        view.setUser(user);
        view.setVideo(video);
        view.setViewDate(new Date());
        viewRepository.save(view);
    }
}
