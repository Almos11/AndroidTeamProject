package com.example.demo.repo;

import com.example.demo.models.UserDataBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataBaseRepository extends JpaRepository<UserDataBase, Long> {
    UserDataBase findByUsername(String username);
}
