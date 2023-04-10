package com.example.demo.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(name = "content")
    private String content;

    @Column(name = "author")
    private String author;

    @Column(name = "creation_time")
    private Date creationTime;

    public Long getId() {
        return id;
    }

    public Video getVideo() {
        return video;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}

