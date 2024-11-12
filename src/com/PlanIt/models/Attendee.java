package com.PlanIt.models;

public class Attendee {
    private int attendeeId;
    private String name;
    private String email;

    // Constructor
    public Attendee(int attendeeId, String name, String email) {
        this.attendeeId = attendeeId;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public int getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(int attendeeId) {
        this.attendeeId = attendeeId;
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
