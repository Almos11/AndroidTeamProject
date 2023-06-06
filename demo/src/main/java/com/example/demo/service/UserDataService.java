package com.example.demo.service;

import com.example.demo.models.UserData;
import com.example.demo.models.UserDataBase;
import com.example.demo.repo.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDataService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;
    public String setupJsonFormatUserData(String token) throws JsonProcessingException {
        UserDataBase user = userRepository.findByToken(token);
        UserData userInfo = new UserData();
        userInfo.setDescription(user.getDescription());
        userInfo.setName(user.getUsername());
        userInfo.setId(user.getId());
        userInfo.setCountLikes(user.getCount_likes());
        userInfo.setCountVideos(user.getCount_videos());
        return objectMapper.writeValueAsString(userInfo);
    }
}
