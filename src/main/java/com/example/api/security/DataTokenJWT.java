package com.example.api.security;

public class DataTokenJWT {

    private String token;

    public DataTokenJWT(){}
    public DataTokenJWT(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
