package com.example.gitapi.services;

import com.example.gitapi.exceptions.ExceededGitApiRateLimit;
import com.example.gitapi.exceptions.UsernameNotFoundException;
import com.example.gitapi.model.GitApiRepositoryResponse;
import com.example.gitapi.model.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.gitapi.GitApiApplication.GIT_API_URL;

@Service
public class RepositoriesService {

    private final RestTemplate rest;

    private final BranchesService branchesService;

    public RepositoriesService(RestTemplate rest, BranchesService branchesService) {
        this.rest = rest;
        this.branchesService = branchesService;
    }

    public List<UserRepository> findRepositoriesByUsernameExcludeFetch(String username) {
        String apiUrl = GIT_API_URL + "users/" + username + "/repos";
        try {
            ResponseEntity<GitApiRepositoryResponse[]> responseEntity = rest.getForEntity(apiUrl, GitApiRepositoryResponse[].class);
            GitApiRepositoryResponse[] body = responseEntity.getBody();
            List<GitApiRepositoryResponse> responseWithoutForked = Collections.emptyList();
            if(body != null)
                responseWithoutForked = Arrays.stream(body).filter(r -> !r.fork()).toList();
            List<UserRepository> userRepositories = mapToUserRepositoryList(responseWithoutForked);
            userRepositories.forEach(repository -> repository.setBranches(branchesService.fetchBranchesForRepository(username, repository.getRepositoryName())));
            return userRepositories;
        } catch (HttpClientErrorException e) {
            HttpStatusCode status = e.getStatusCode();
            if (status == HttpStatus.NOT_FOUND)
                throw new UsernameNotFoundException(status, "Username: " + username + " not found");
            else
                throw new ExceededGitApiRateLimit(status, "Error fetching data from Git Api, exceeded rate limit");
        }
    }

    private List<UserRepository> mapToUserRepositoryList(List<GitApiRepositoryResponse> responseWithoutForked) {
        return responseWithoutForked.stream().map(r -> new UserRepository(r.name(), r.login())).collect(Collectors.toList());
    }

}
