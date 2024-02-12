package com.example.gitapi.model;

public record GitApiRepositoryResponse(String name, Owner owner, boolean fork) {

    public String login(){
        return owner().login();
    }

    public record Owner(String login){

    }
}
