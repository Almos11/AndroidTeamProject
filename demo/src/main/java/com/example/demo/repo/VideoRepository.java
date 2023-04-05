package com.example.demo.repo;

import com.example.demo.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Transactional
    Video findVideoByIdentifier(String identifier);

    @Transactional
    List<Video> findAll();

    @Transactional
    @Query(value = "SELECT identifier FROM videos ORDER BY LOG(rating) DESC LIMIT 1", nativeQuery = true)
    String getTopRatedVideoId();

}
