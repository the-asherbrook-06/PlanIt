package com.PlanIt.models;

import java.util.Date;

public class Session {
    private int sessionId;
    private String title;
    private String description;
    private Date sessionTime;
    private Speaker speaker;

    // Constructor
    public Session(int sessionId, String title, String description, Date sessionTime, Speaker speaker) {
        this.sessionId = sessionId;
        this.title = title;
        this.description = description;
        this.sessionTime = sessionTime;
        this.speaker = speaker;
    }

    // Getters and Setters
    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(Date sessionTime) {
        this.sessionTime = sessionTime;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }
}
