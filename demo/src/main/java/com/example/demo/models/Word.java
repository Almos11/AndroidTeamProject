package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String value;

    public Word() {}

    public Word(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public Long getId() {
        return id;
    }

}

