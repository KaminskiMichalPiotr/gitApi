package com.example.gitapi.model;

import org.springframework.http.HttpStatusCode;

public record ErrorResponse(String message, HttpStatusCode status) {

}
