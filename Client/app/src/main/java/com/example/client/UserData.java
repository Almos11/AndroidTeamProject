package com.example.client;

public class UserData {
    private Long id;
    private String name;
    private String description;
    private int countLikes;
    private int countVideos;

    // Конструкторы, геттеры и сеттеры

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


