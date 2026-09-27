package com.Project.Url_Shortener.Configuration;

import com.Project.Url_Shortener.Service.Jwtservice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final Jwtservice jwtservice;

    public SecurityConfig(Jwtservice jwtservice) {
        this.jwtservice = jwtservice;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.
                withSecretKey(jwtservice.getKey())
                .build();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrt->csrt.disable())
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers("/auth/register",
                                "/auth/login"
                        ).permitAll()
                        .requestMatchers("project/delete/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2
                ->oauth2.jwt(jwt->{})
                        );
        return httpSecurity.build();
    }
}
