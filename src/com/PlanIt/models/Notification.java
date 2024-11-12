package com.PlanIt.models;

import java.util.Date;

public class Notification {
    private int notificationId;
    private String message;
    private Date sentTime;
    private Attendee attendee;
    private Session session;

    // Constructor
    public Notification(int notificationId, String message, Date sentTime, Attendee attendee, Session session) {
        this.notificationId = notificationId;
        this.message = message;
        this.sentTime = sentTime;
        this.attendee = attendee;
        this.session = session;
    }

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
