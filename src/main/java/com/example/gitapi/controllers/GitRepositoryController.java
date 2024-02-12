package com.example.gitapi.controllers;

import com.example.gitapi.model.UserRepository;
import com.example.gitapi.services.RepositoriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/repositories")
public class GitRepositoryController {

    private final RepositoriesService repositoriesService;

    public GitRepositoryController(RepositoriesService repositoriesService) {
        this.repositoriesService = repositoriesService;
    }

    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<List<UserRepository>> getRepositoriesWithBranchesByUsername(@PathVariable String username){
        List<UserRepository> repositoriesByUsernameExcludeFetch = repositoriesService.findRepositoriesByUsernameExcludeFetch(username);
        return ResponseEntity.ok().body(repositoriesByUsernameExcludeFetch);
    }

}
