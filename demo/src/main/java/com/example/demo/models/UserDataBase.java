package com.example.demo.models;


import jakarta.persistence.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
public class UserDataBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "description")
    private String description;

    @Column(name = "count_videos")
    private Integer count_videos;

    @Column(name = "count_likes")
    private Integer count_likes;

    @Column(name = "creationTime")
    private Instant creationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreationTime() {
        creationTime = Instant.now();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        setCreationTime();
    }

    public boolean isTokenExpired(int tokenExpirationSeconds) {
        Instant expirationTime = creationTime.plus(tokenExpirationSeconds, ChronoUnit.SECONDS);
        return Instant.now().isAfter(expirationTime);
    }

    @OneToMany(mappedBy = "user")
    private List<LikedVideo> likedVideos;

    public List<LikedVideo> getLikedVideos() {
        return likedVideos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCount_videos() {
        return count_videos;
    }

    public void setCount_videos(Integer count_videos) {
        this.count_videos = count_videos;
    }

    public Integer getCount_likes() {
        return count_likes;
    }

    public void setCount_likes(Integer count_likes) {
        this.count_likes = count_likes;
    }
}
