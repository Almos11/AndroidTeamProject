package com.example.demo.models;
public class VideoData {
    String id;
    String author_id;
    String author_name;
    int likes;
    int comments;
    int views;
    int rating;

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

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getId() {
        return this.id;
    }

    public String getAuthor_name() {
        return this.author_name;
    }

    public int getLikes() {
        return this.likes;
    }

    public int getComments() {
        return this.comments;
    }

    public int getViews() {
        return this.views;
    }

    public int getRating() {
        return this.rating;
    }
}
