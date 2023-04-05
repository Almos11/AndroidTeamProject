package com.example.demo.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "views")
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDataBase user;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(name = "view_date")
    private Date viewDate;

    public Long getId() {
        return id;
    }

    public UserDataBase getUser() {
        return user;
    }

    public Video getVideo() {
        return video;
    }

    public Date getViewDate() {
        return viewDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserDataBase user) {
        this.user = user;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public void setViewDate(Date viewDate) {
        this.viewDate = viewDate;
    }
}

