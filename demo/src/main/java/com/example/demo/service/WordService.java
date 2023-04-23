package com.example.demo.service;

import com.example.demo.models.Word;
import com.example.demo.repo.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordService {
    @Autowired
    private WordRepository wordRepository;
    public void saveWords(List<String> words) {
        for (String word : words) {
            wordRepository.save(new Word(word));
        }
    }
}
