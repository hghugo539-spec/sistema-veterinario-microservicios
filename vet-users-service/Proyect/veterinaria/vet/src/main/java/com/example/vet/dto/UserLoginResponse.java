package com.example.vet.dto;

public class UserLoginResponse {
    private String token;

    public UserLoginResponse() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}