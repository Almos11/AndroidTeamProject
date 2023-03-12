package com.example.demo.repo;

import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Transactional
    public Video findVideoByName(String nameVideo);

    @Transactional
    public Video findVideoById(Long id);



    public List<Video> findAllByUserDataBase(UserDataBase user);
}
