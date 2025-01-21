package com.example.eventmanagementapp.model;

public class EventUsers {
    private String userId;

    public EventUsers() {

    }
    public EventUsers(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
