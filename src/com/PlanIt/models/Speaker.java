package com.PlanIt.models;

public class Speaker {
    private int speakerId;
    private String name;
    private String email;

    // Constructor
    public Speaker(int speakerId, String name, String email) {
        this.speakerId = speakerId;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public int getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(int speakerId) {
        this.speakerId = speakerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
