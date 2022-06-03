package com.project.elisa.dao;

import com.project.elisa.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User,Integer> {
    public User findByUsername(String username);
    public Optional<User> findByUsernameAndPassword(String username, String password);
    public User findById(int userId);
}
