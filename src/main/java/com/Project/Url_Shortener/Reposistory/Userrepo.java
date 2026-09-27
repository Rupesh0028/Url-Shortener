package com.Project.Url_Shortener.Reposistory;

import com.Project.Url_Shortener.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Userrepo extends JpaRepository<User,Long> {
    Optional<User> findByusername(String user);
}
