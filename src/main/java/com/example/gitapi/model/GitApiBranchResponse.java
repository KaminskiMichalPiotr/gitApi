package com.example.gitapi.model;

public record GitApiBranchResponse(String name, Commit commit) {

    public String sha(){
        return commit().sha();
    }

    public record Commit(String sha){}
}

