package com.PlanIt.service;

import com.PlanIt.models.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class NotificationService {

    // Add Notification
    public void addNotification(Notification notification) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Notification (Message, AttendeeID, SessionID, SentTime) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, notification.getMessage());
            stmt.setInt(2, notification.getAttendee().getAttendeeId());
            stmt.setInt(3, notification.getSession().getSessionId());
            stmt.setTimestamp(4, new Timestamp(notification.getSentTime().getTime()));
            stmt.executeUpdate();
            System.out.println("Notification added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modify Notification
    public void modifyNotification(int notificationId, String newMessage) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Notification SET Message = ? WHERE NotificationID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newMessage);
            stmt.setInt(2, notificationId);
            stmt.executeUpdate();
            System.out.println("Notification modified successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Notification
    public void deleteNotification(int notificationId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM Notification WHERE NotificationID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
            System.out.println("Notification deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
