package com.Project.Url_Shortener.Service;

import com.Project.Url_Shortener.Entity.User;
import com.Project.Url_Shortener.Reposistory.Userrepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetails implements UserDetailsService{

    private final Userrepo userrepo;

    public CustomUserDetails(Userrepo userrepo) {
        this.userrepo = userrepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userrepo.findByusername(username)
                .orElseThrow(()->new UsernameNotFoundException("Not Found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
