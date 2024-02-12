package com.example.gitapi.services;

import com.example.gitapi.exceptions.ExceededGitApiRateLimit;
import com.example.gitapi.exceptions.UsernameNotFoundException;
import com.example.gitapi.model.GitApiRepositoryResponse;
import com.example.gitapi.model.UserRepository;
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

class RepositoriesServiceTest {


    @Mock
    private BranchesService branchesServiceMock;

    @Mock
    private RestTemplate restTemplateMock;

    private RepositoriesService repositoriesService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        repositoriesService = new RepositoriesService(restTemplateMock, branchesServiceMock);
    }


    @Test
    public void testFindRepositoriesByUsernameExcludeFetch_Successful() {
        // Given
        GitApiRepositoryResponse[] mockResponse = {
                new GitApiRepositoryResponse("repo1", new GitApiRepositoryResponse.Owner("Owner1"), false),
                new GitApiRepositoryResponse("repo2", new GitApiRepositoryResponse.Owner("Owner2"), false)
        };
        ResponseEntity<GitApiRepositoryResponse[]> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        // When
        when(restTemplateMock.getForEntity(anyString(), eq(GitApiRepositoryResponse[].class))).thenReturn(responseEntity);
        when(branchesServiceMock.fetchBranchesForRepository(anyString(), anyString())).thenReturn(Collections.emptyList());
        List<UserRepository> result = repositoriesService.findRepositoriesByUsernameExcludeFetch("username");

        // Then
        assertEquals(2, result.size());
        assertEquals("repo1", result.get(0).getRepositoryName());
        assertEquals("Owner1", result.get(0).getOwnerLogin());
        assertEquals("repo2", result.get(1).getRepositoryName());
        assertEquals("Owner2", result.get(1).getOwnerLogin());
    }

    @Test
    public void testFindRepositoriesByUsernameExcludeFetch_UserNotFound() {
        // When
        when(restTemplateMock.getForEntity(anyString(), any())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Then
        HttpClientErrorException exception = assertThrows(UsernameNotFoundException.class, () ->
                repositoriesService.findRepositoriesByUsernameExcludeFetch("nonexistent_user"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void testFindRepositoriesByUsernameExcludeFetch_RateLimitExceeded() {
        // When
        when(restTemplateMock.getForEntity(anyString(), any())).thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        // Then
        HttpClientErrorException exception = assertThrows(ExceededGitApiRateLimit.class, () ->
                repositoriesService.findRepositoriesByUsernameExcludeFetch("username"));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }
}