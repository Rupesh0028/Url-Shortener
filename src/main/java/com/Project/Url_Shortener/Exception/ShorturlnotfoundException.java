package com.Project.Url_Shortener.Exception;

public class ShorturlnotfoundException extends RuntimeException{
    public ShorturlnotfoundException(String notFound) {
        super(notFound);
    }
}
