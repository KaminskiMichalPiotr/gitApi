package com.example.gitapi.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;

public class ExceededGitApiRateLimit extends HttpClientErrorException {

    public ExceededGitApiRateLimit(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

}
