package com.Project.Url_Shortener.Service;

import com.Project.Url_Shortener.DTO.UrlResponseDto;
import com.Project.Url_Shortener.DTO.urlDto;
import com.Project.Url_Shortener.DTO.urlRequest;
import com.Project.Url_Shortener.Entity.Url;
import com.Project.Url_Shortener.Entity.User;
import com.Project.Url_Shortener.Exception.ShorturlnotfoundException;
import com.Project.Url_Shortener.Reposistory.Userrepo;
import com.Project.Url_Shortener.Reposistory.repo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class urlService {
    private final static String alphabets="qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM12345690";
    private final static SecureRandom random=new SecureRandom();
    private final repo rep;
    private final Userrepo userrepo;


    public urlService(repo rep, Userrepo userrepo) {
        this.rep = rep;
        this.userrepo = userrepo;
    }

    public UrlResponseDto createUrl(urlRequest request, Authentication authentication){
       String shortUrl=generates();

       while (rep.findByshortUrl(shortUrl).isPresent()){
           shortUrl=generates();
       }
       User user=userrepo.findByusername(authentication.getName())
               .orElseThrow(()
               ->new UsernameNotFoundException("not found"));
       Url url=new Url(request.getLongurl(),shortUrl);
       url.setUser(user);
       Url saved =rep.save(url);
       return new UrlResponseDto(saved);

    }
    public Optional<Url> getStats(String shortUrl){
        return rep.findByshortUrl(shortUrl);
    }
    public String getOriginalurl(String shortUrl){
        Url url=rep.findByshortUrl(shortUrl).
                orElseThrow(()->new ShorturlnotfoundException("Not Found"));
        url.setClick(url.getClick()+1);
        rep.save(url);
        return url.getLongUrl();
    }
    public UrlResponseDto updateUrl(
            String shortUrl,
            urlRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        Url url = rep
                .findByShortUrlAndUserUsername(
                        shortUrl,
                        username
                )
                .orElseThrow(
                        () -> new ShorturlnotfoundException(
                                "URL not found or you are not the owner"
                        )
                );

        url.setLongUrl(request.getLongurl());

        url.setUpdatedAt(LocalDateTime.now());

        Url updatedUrl = rep.save(url);

        return new UrlResponseDto(updatedUrl);
    }
    public void deleteUrl(
            String shortUrl,
            Authentication authentication) {

        String username = authentication.getName();

        Url url = rep
                .findByShortUrlAndUserUsername(
                        shortUrl,
                        username
                )
                .orElseThrow(
                        () -> new ShorturlnotfoundException(
                                "URL not found or you are not the owner"
                        )
                );

        rep.delete(url);
    }
    public String generates(){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<6;i++){
            sb.append(alphabets.charAt(random.nextInt(alphabets.length())));
        }
        return sb.toString();
    }

}
