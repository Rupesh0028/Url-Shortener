package com.Project.Url_Shortener.Exception;

import com.Project.Url_Shortener.DTO.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionalhandler {

    @ExceptionHandler(ShorturlnotfoundException.class)
     public ResponseEntity<ErrorResponseDto> urlnotfound(ShorturlnotfoundException ex){
         ErrorResponseDto errorResponseDto=new ErrorResponseDto(ex.getMessage(), 404, LocalDateTime.now());
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
     }
}
