package com.example.demo.repo;

import com.example.demo.models.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    @Query(value = "SELECT value FROM words ORDER BY RANDOM() LIMIT 3", nativeQuery = true)
    List<String> findRandomWords();
}
