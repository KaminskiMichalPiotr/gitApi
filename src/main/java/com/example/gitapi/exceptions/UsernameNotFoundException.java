package com.example.gitapi.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

public class UsernameNotFoundException extends HttpClientErrorException {
    public UsernameNotFoundException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }
}
