package com.Project.Url_Shortener.Reposistory;

import com.Project.Url_Shortener.Entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface repo extends JpaRepository<Url,Long> {
    Optional<Url> findByshortUrl(String shortUrl);
    Optional<Url> findByShortUrlAndUserUsername(
            String shortUrl,
            String username
    );
}
