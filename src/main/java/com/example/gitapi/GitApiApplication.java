package com.example.gitapi;

import com.example.gitapi.model.GitApiRepositoryResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GitApiApplication {

	public static final String GIT_API_URL = "https://api.github.com/";

	public static void main(String[] args) {
		SpringApplication.run(GitApiApplication.class, args);
	}

}
