package com.example.demo.repo;

import com.example.demo.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Transactional
    Video findVideoByIdentifier(String identifier);

    @Transactional
    List<Video> findAll();
   @Query(value = "SELECT identifier " +
           "FROM videos " +
           "WHERE NOT EXISTS (" +
           "    SELECT 1 " +
           "    FROM views " +
           "    WHERE views.video_id = videos.id AND views.user_id = :userId" +
           ")" +
           "ORDER BY LOG(rating + 100) * RANDOM() DESC LIMIT 1", nativeQuery = true)
   String getTopRatedVideoId(@Param("userId") Long userId);

}
