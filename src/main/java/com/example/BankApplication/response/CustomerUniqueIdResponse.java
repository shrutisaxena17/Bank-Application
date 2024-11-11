package com.example.BankApplication.response;

public class CustomerUniqueIdResponse {
    String message;
    String uniqueId;
    String jwt;

    public CustomerUniqueIdResponse(String message, String uniqueId, String jwt) {
        this.message = message;
        this.uniqueId = uniqueId;
        this.jwt = jwt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
