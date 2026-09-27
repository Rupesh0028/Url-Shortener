package com.Project.Url_Shortener.Service;

import com.Project.Url_Shortener.DTO.LoginRequestDto;
import com.Project.Url_Shortener.DTO.LoginResponseDto;
import com.Project.Url_Shortener.DTO.RegisterRequestDto;
import com.Project.Url_Shortener.Entity.User;
import com.Project.Url_Shortener.Reposistory.Userrepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final Userrepo userrepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Jwtservice jwtservice;

    public UserService(Userrepo userrepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, Jwtservice jwtservice) {
        this.userrepo = userrepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtservice = jwtservice;
    }
    public LoginResponseDto login(LoginRequestDto request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userrepo
                .findByusername(request.getUsername())
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "User not found"
                        )
                );

        String token =
                jwtservice.generatetoken(
                        user.getUsername()
                );

        return new LoginResponseDto(
                token,
                user.getUsername(),
                user.getRole()
        );
    }


    public User register(RegisterRequestDto registerRequestDto){
        User user=new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole("USER");
        return userrepo.save(user);
    }
}
