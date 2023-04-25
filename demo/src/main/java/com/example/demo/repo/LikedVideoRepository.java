package com.example.demo.repo;

import com.example.demo.models.LikedVideo;
import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedVideoRepository extends JpaRepository<LikedVideo, Long> {
    LikedVideo findByUserAndVideo(UserDataBase user, Video video);
}
