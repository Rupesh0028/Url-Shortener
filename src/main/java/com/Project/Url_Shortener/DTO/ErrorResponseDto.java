package com.Project.Url_Shortener.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorResponseDto {

        private String message;
        private int status;
        private LocalDateTime timestamp;

        public ErrorResponseDto(
                String message,
                int status,
                LocalDateTime timestamp) {

            this.message = message;
            this.status = status;
            this.timestamp = timestamp;
        }

}
