package com.Project.Url_Shortener.DTO;

import com.Project.Url_Shortener.Entity.Url;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UrlResponseDto {

    private String longUrl;
    private String shortUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long click;

    public UrlResponseDto(Url url) {
        this.longUrl = url.getLongUrl();
        this.shortUrl = url.getShortUrl();
        this.createdAt = url.getCreatedAt();
        this.updatedAt = url.getUpdatedAt();
        this.click = url.getClick();
    }
}