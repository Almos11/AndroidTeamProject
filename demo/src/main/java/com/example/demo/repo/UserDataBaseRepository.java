package com.example.demo.repo;

import com.example.demo.models.UserDataBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataBaseRepository extends JpaRepository<UserDataBase, Long> {

}
