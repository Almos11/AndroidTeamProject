package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "data")
    private byte[] data;

    public void setData(byte[] data) {
        this.data = data;
    }

    @Column(name = "name")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "content_type")
    private String contentType;

    public void setContentType (String contentType) {
        this.contentType = contentType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setUser (User user) {
        this.user = user;
    }
}