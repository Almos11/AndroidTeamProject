package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    @Lob
    @Column(name = "data")
    private byte[] data;

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Column(name = "rating")
    private Integer rating;

    public void updateRating() {
        this.rating = Math.max(this.countLike - this.countDislike, 0);
    }

    public int getRating() {
        return rating;
    }

    @Column(name = "identifier")
    private String identifier;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    @Column(name = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "countLike")
    private Integer countLike;
    public void setCountLike() {
        this.countLike = 0;
    }

    public void decreaseCountLike() {
        this.countLike--;
    }

    public void increaseCountLike() {
        this.countLike++;
    }

    @Column(name = "countDislike")
    private Integer countDislike;

    public void setCountDislike() {
        this.countDislike = 0;
    }

    public void decreaseCountDislike() {
        this.countDislike--;
    }

    public void increaseCountDislike() {
        this.countDislike++;
    }


    @Column(name = "content_type")
    private String contentType;

    public void setContentType (String contentType) {
        this.contentType = contentType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserDataBase userDataBase;

    public void setUser (UserDataBase userDataBase) {
        this.userDataBase = userDataBase;
    }
}