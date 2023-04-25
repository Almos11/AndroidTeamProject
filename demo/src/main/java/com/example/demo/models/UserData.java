package com.example.demo.models;

public class UserData {
    Long id;
    String name;

    String description;
    int countLikes;
    int countVideos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(int countLikes) {
        this.countLikes = countLikes;
    }

    public int getCountVideos() {
        return countVideos;
    }

    public void setCountVideos(int countVideos) {
        this.countVideos = countVideos;
    }
}
