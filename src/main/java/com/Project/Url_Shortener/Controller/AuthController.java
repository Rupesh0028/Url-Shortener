package com.Project.Url_Shortener.Controller;

import com.Project.Url_Shortener.DTO.LoginRequestDto;
import com.Project.Url_Shortener.DTO.LoginResponseDto;
import com.Project.Url_Shortener.DTO.RegisterRequestDto;
import com.Project.Url_Shortener.Entity.User;
import com.Project.Url_Shortener.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<User> Register(@RequestBody RegisterRequestDto registerRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(registerRequestDto));

    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto request) {

        LoginResponseDto response =
                userService.login(request);

        return ResponseEntity.ok(response);
    }
}
