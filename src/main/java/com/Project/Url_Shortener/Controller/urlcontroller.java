package com.Project.Url_Shortener.Controller;

import com.Project.Url_Shortener.DTO.UrlResponseDto;
import com.Project.Url_Shortener.DTO.urlDto;
import com.Project.Url_Shortener.DTO.urlRequest;
import com.Project.Url_Shortener.Entity.Url;
import com.Project.Url_Shortener.Service.urlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/project")
public class urlcontroller {
    private final urlService urlservice;

    public urlcontroller(urlService urlservice) {
        this.urlservice = urlservice;
    }
    @PostMapping("/create")
    public ResponseEntity<UrlResponseDto> createUrl(@RequestBody urlRequest request, Authentication authentication){
        return ResponseEntity.status(HttpStatus.CREATED).body(urlservice.createUrl(request,authentication));
    }
    @GetMapping("getStats/{longUrl}")
    public ResponseEntity<Optional<Url>> getStat(@PathVariable String shortUrl){
        return ResponseEntity.status(HttpStatus.FOUND).body(urlservice.getStats(shortUrl));
    }
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> getOrginalUrl(@PathVariable String shortUrl){
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlservice.getOriginalurl(shortUrl))).build();
    }
    @PutMapping("/update/{shortUrl}")
    public ResponseEntity<UrlResponseDto> updateUrl(
            @PathVariable String shortUrl,
            @RequestBody urlRequest request,
            Authentication authentication) {

        UrlResponseDto response =
                urlservice.updateUrl(
                        shortUrl,
                        request,
                        authentication
                );

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{shortUrl}")
    public ResponseEntity<Void> delete(
            @PathVariable String shortUrl,
            Authentication authentication) {

        urlservice.deleteUrl(
                shortUrl,
                authentication
        );

        return ResponseEntity.noContent().build();
    }

}
