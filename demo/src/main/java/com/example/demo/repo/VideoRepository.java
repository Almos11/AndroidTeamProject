package com.example.demo.repo;

import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    public Video findByName(String nameVideo);

    public List<Video> findAllByUserDataBase(UserDataBase user);
}
