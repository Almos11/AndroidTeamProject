package com.example.demo.repo;

import com.example.demo.models.DislikedVideo;
import com.example.demo.models.LikedVideo;
import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DislikedVideoRepository extends JpaRepository<DislikedVideo, Long> {
    DislikedVideo findByUserAndVideo(UserDataBase user, Video video);
}
