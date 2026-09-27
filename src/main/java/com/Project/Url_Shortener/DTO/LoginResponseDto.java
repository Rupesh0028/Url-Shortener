package com.Project.Url_Shortener.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String token;

    private String username;

    private String role;

    public LoginResponseDto(
            String token,
            String username,
            String role) {

        this.token = token;
        this.username = username;
        this.role = role;
    }
}