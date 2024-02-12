package com.example.gitapi.services;

import com.example.gitapi.model.GitApiBranchResponse;
import com.example.gitapi.model.RepositoryBranches;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class BranchesServiceTest {

    private BranchesService branchesService;

    @Mock
    private RestTemplate restTemplateMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        branchesService = new BranchesService(restTemplateMock);
    }

    @Test
    public void testFetchBranchesForRepository_Successful() {
        // Given
        GitApiBranchResponse[] mockResponse = {new GitApiBranchResponse("branch1", new GitApiBranchResponse.Commit("sha1")),
                new GitApiBranchResponse("branch2", new GitApiBranchResponse.Commit("sha2"))};
        ResponseEntity<GitApiBranchResponse[]> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        // When
        when(restTemplateMock.getForEntity(anyString(), eq(GitApiBranchResponse[].class))).thenReturn(responseEntity);
        List<RepositoryBranches> result = branchesService.fetchBranchesForRepository("username", "repositoryName");

        // Then
        assertEquals(2, result.size());
        assertEquals("branch1", result.get(0).branchName());
        assertEquals("sha1", result.get(0).commitSHA());
        assertEquals("branch2", result.get(1).branchName());
        assertEquals("sha2", result.get(1).commitSHA());
    }

    @Test
    public void testFetchBranchesForRepository_RateLimitExceeded() {
        // When
        when(restTemplateMock.getForEntity(anyString(), any())).thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        // Then
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () ->
                branchesService.fetchBranchesForRepository("username", "repositoryName"));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    public void testFetchBranchesForRepository_EmptyResponse() {
        // Given
        ResponseEntity<GitApiBranchResponse[]> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        // When
        when(restTemplateMock.getForEntity(anyString(), eq(GitApiBranchResponse[].class))).thenReturn(responseEntity);
        List<RepositoryBranches> result = branchesService.fetchBranchesForRepository("username", "repositoryName");

        // Then
        assertEquals(Collections.emptyList(), result);
    }

}