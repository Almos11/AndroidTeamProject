package com.example.demo.repo;

import com.example.demo.models.UserDataBase;
import com.example.demo.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    @Transactional
    public Video findVideoByName(String nameVideo);

    @Transactional
    public Video findVideoById(Long id);


    @Transactional
    public Video findVideoByUserDataBaseAndId(UserDataBase user, Long id);

    @Transactional
    public Video findVideoByIdentifier(String identifier);

    @Transactional
    public List<Video> findAll();

    /*@Transactional
    @Query(value = "UPDATE videos SET position = subquery.position " +
            "FROM (SELECT id, ROW_NUMBER() OVER (ORDER BY rating DESC) " +
            "as position FROM videos) AS subquery WHERE videos.id = subquery.id",
            nativeQuery = true)
    void updateVideos();

    @Transactional
    @Query(value = "SELECT * FROM videos ORDER BY LOG(rating) DESC", nativeQuery = true)
    List<Video> getSortedVideos();*/

    @Transactional
    @Query(value = "SELECT identifier FROM videos ORDER BY LOG(rating) DESC LIMIT 1", nativeQuery = true)
    String getTopRatedVideoId();

}
