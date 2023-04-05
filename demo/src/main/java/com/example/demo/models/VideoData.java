package com.example.demo.models;

public class VideoData {
    String id;
    String author_id;
    String author_name;
    int likes;
    int comments;
    int views;

    public void setId(String id) {
        this.id = id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
