package com.example.gitapi.services;

import com.example.gitapi.exceptions.ExceededGitApiRateLimit;
import com.example.gitapi.model.GitApiBranchResponse;
import com.example.gitapi.model.RepositoryBranches;
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
public class BranchesService {

    private final RestTemplate rest;

    public BranchesService(RestTemplate rest) {
        this.rest = rest;
    }

    public List<RepositoryBranches> fetchBranchesForRepository(String username, String repositoryName) {
        String apiUrl = GIT_API_URL + "repos/" + username + "/" + repositoryName + "/branches";
        try {
            ResponseEntity<GitApiBranchResponse[]> responseEntity = rest.getForEntity(apiUrl, GitApiBranchResponse[].class);
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                return Collections.emptyList();
            }
            GitApiBranchResponse[] body = responseEntity.getBody();
            List<GitApiBranchResponse> branchesApiResponse = Collections.emptyList();
            if(body != null)
                branchesApiResponse = Arrays.stream(body).toList();

            return mapToRepositoryBranchesList(branchesApiResponse);
        } catch (HttpClientErrorException e) {
            throw new ExceededGitApiRateLimit(e.getStatusCode(), "Error fetching data from Git Api, exceeded rate limit");
        }
    }

    private List<RepositoryBranches> mapToRepositoryBranchesList(List<GitApiBranchResponse> branchesApiResponse) {
        return branchesApiResponse.stream().map(b -> new RepositoryBranches(b.name(), b.sha())).collect(Collectors.toList());
    }
}
