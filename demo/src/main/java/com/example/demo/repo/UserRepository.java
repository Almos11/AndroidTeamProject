package com.example.demo.repo;

import com.example.demo.models.UserDataBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDataBase, Long> {
    public UserDataBase findByUsername(String username);
    public UserDataBase findByToken(String token);

    public UserDataBase findUserDataBaseById(Long user_id);
}
