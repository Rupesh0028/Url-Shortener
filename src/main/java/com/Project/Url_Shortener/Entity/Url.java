package com.Project.Url_Shortener.Entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Target;
import java.time.LocalDateTime;

@Getter
@Setter
@Table
@Entity
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,length = 2048)
    private String longUrl;

    @Column(nullable = false,unique = true,length = 10)
    private String shortUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private long click=0;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Url() {
    }

    public Url(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
