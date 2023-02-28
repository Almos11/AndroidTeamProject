package com.example.demo.repo;

import com.example.demo.models.UserDataBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDataBaseRepository extends JpaRepository<UserDataBase, Long> {
    public UserDataBase findByUsername(String username);
    public UserDataBase findByToken(String token);
}
