CREATE DATABASE PlanItDB;
USE PlanItDB;

CREATE TABLE Speaker (
    SpeakerID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE Session (
    SessionID INT AUTO_INCREMENT PRIMARY KEY,
    Title VARCHAR(100) NOT NULL,
    Description TEXT,
    SessionTime DATETIME NOT NULL,
    SpeakerID INT,
    FOREIGN KEY (SpeakerID) REFERENCES Speaker(SpeakerID)
);

CREATE TABLE Attendee (
    AttendeeID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE AttendeeSession (
    AttendeeID INT,
    SessionID INT,
    PRIMARY KEY (AttendeeID, SessionID),
    FOREIGN KEY (AttendeeID) REFERENCES Attendee(AttendeeID),
    FOREIGN KEY (SessionID) REFERENCES Session(SessionID)
);

CREATE TABLE Notification (
    NotificationID INT AUTO_INCREMENT PRIMARY KEY,
    Message TEXT NOT NULL,
    AttendeeID INT,
    SessionID INT,
    SentTime DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AttendeeID) REFERENCES Attendee(AttendeeID),
    FOREIGN KEY (SessionID) REFERENCES Session(SessionID)
);
