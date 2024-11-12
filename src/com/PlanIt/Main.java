package com.PlanIt;

import com.PlanIt.models.*;
import com.PlanIt.service.ScheduleService;
import com.PlanIt.service.NotificationService;
import com.PlanIt.service.DatabaseConnection;

import java.sql.*;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static ScheduleService scheduleService = new ScheduleService();
    private static NotificationService notificationService = new NotificationService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            clearScreen();
            System.out.println("Welcome to the Multi-Event Scheduling System");
            System.out.println("1. Register Speaker");
            System.out.println("2. Modify Speaker");
            System.out.println("3. Remove Speaker");
            System.out.println("4. Register Attendee");
            System.out.println("5. Modify Attendee");
            System.out.println("6. Remove Attendee");
            System.out.println("7. Create Session");
            System.out.println("8. Register Speaker to Session");
            System.out.println("9. Unregister Speaker from Session");
            System.out.println("10. Register Attendee to Session");
            System.out.println("11. Unregister Attendee from Session");
            System.out.println("12. Send Notification");
            System.out.println("13. Modify Notification");
            System.out.println("14. Delete Notification");
            System.out.println("15. View All Speakers");
            System.out.println("16. View All Attendees");
            System.out.println("17. View Upcoming Events");
            System.out.println("18. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerSpeaker();
                    break;
                case 2:
                    modifySpeaker();
                    break;
                case 3:
                    deleteSpeaker();
                    break;
                case 4:
                    registerAttendee();
                    break;
                case 5:
                    modifyAttendee();
                    break;
                case 6:
                    deleteAttendee();
                    break;
                case 7:
                    addSession();
                    break;
                case 8:
                    assignSpeakerToSession();
                    break;
                case 9:
                    unassignSpeakerFromSession();
                    break;
                case 10:
                    registerAttendeeToSession();
                    break;
                case 11:
                    unregisterAttendeeFromSession();
                    break;
                case 12:
                    sendNotification();
                    break;
                case 13:
                    modifyNotification();
                    break;
                case 14:
                    deleteNotification();
                    break;
                case 15:
                    scheduleService.displayAllSpeakers();
                    break;
                case 16:
                    scheduleService.displayAllAttendees();
                    break;
                case 17:
                    System.out.print("Enter the number of events to display: ");
                    int numEvents = scanner.nextInt();
                    scheduleService.displayUpcomingEvents(numEvents);
                    break;
                case 18:
                    System.out.println("Exiting the program...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
            System.out.println("Press Enter to continue...");
            scanner.nextLine(); // Wait for user to press Enter
        }
    }

    // Method to clear the screen (requires 'chalk' dependency)
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Method to add a session
    public static void addSession() {
        System.out.print("Enter session title: ");
        String title = scanner.nextLine();
        System.out.print("Enter session description: ");
        String description = scanner.nextLine();
        System.out.print("Enter session time (YYYY-MM-DD HH:MM): ");
        String sessionTimeStr = scanner.nextLine();

        // Parse session time
        Timestamp sessionTime = Timestamp.valueOf(sessionTimeStr + ":00");

        // Allow a null speaker ID
        Integer speakerID = null;
        System.out.print("Enter Speaker ID (leave empty if no speaker): ");
        String speakerIdStr = scanner.nextLine();

        if (!speakerIdStr.trim().isEmpty()) {
            speakerID = Integer.parseInt(speakerIdStr);
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Session (Title, Description, SessionTime, SpeakerID) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setTimestamp(3, sessionTime);
            stmt.setObject(4, speakerID, java.sql.Types.INTEGER); // Allow for null value

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Session added successfully!");
            } else {
                System.out.println("Failed to add session.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to register a speaker
    private static void registerSpeaker() {
        clearScreen();
        System.out.print("Enter speaker name: ");
        String name = scanner.nextLine();
        System.out.print("Enter speaker email: ");
        String email = scanner.nextLine();

        Speaker speaker = new Speaker(0, name, email);  // Assuming Speaker has an ID of 0 initially
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Speaker (Name, Email) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Speaker registered successfully!");
            } else {
                System.out.println("Failed to register speaker.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to modify speaker details
    private static void modifySpeaker() {
        clearScreen();
        System.out.print("Enter speaker ID to modify: ");
        int speakerId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter new speaker name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new speaker email: ");
        String newEmail = scanner.nextLine();

        scheduleService.modifySpeaker(speakerId, newName, newEmail);
    }

    // Method to delete speaker
    private static void deleteSpeaker() {
        clearScreen();
        System.out.print("Enter speaker ID to delete: ");
        int speakerId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        scheduleService.deleteSpeaker(speakerId);
    }

    // Method to register an attendee
    private static void registerAttendee() {
        clearScreen();
        System.out.print("Enter attendee name: ");
        String name = scanner.nextLine();
        System.out.print("Enter attendee email: ");
        String email = scanner.nextLine();
        Attendee attendee = new Attendee(0, name, email);

        scheduleService.addAttendee(attendee);
        System.out.println("Attendee registered successfully!");
    }

    // Method to modify attendee details
    private static void modifyAttendee() {
        clearScreen();
        System.out.print("Enter attendee ID to modify: ");
        int attendeeId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter new attendee name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new attendee email: ");
        String newEmail = scanner.nextLine();

        scheduleService.modifyAttendee(attendeeId, newName, newEmail);
    }

    // Method to delete attendee
    private static void deleteAttendee() {
        clearScreen();
        System.out.print("Enter attendee ID to delete: ");
        int attendeeId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        scheduleService.deleteAttendee(attendeeId);
    }

    // Method to register an attendee to a session
    private static void registerAttendeeToSession() {
        clearScreen();
        System.out.print("Enter attendee ID to register: ");
        int attendeeId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter session ID to register attendee to: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        scheduleService.registerAttendee(attendeeId, sessionId);
    }

    // Method to unregister an attendee from a session
    private static void unregisterAttendeeFromSession() {
        clearScreen();
        System.out.print("Enter attendee ID to unregister: ");
        int attendeeId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter session ID to unregister attendee from: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        scheduleService.unregisterAttendee(attendeeId, sessionId);
    }

    // Method to assign a speaker to a session
    private static void assignSpeakerToSession() {
        clearScreen();
        System.out.print("Enter speaker ID to assign: ");
        int speakerId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter session ID to assign speaker to: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Speaker speaker = scheduleService.getSpeakerById(speakerId);
        Session session = scheduleService.getSessionById(sessionId);

        if (speaker != null && session != null) {
            scheduleService.assignSpeaker(session, speaker);
            System.out.println("Speaker assigned successfully!");
        } else {
            System.out.println("Invalid speaker or session ID.");
        }
    }

    // Method to unassign speaker from session
    private static void unassignSpeakerFromSession() {
        clearScreen();
        System.out.print("Enter session ID to unassign speaker from: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        scheduleService.unassignSpeaker(sessionId);
        System.out.println("Speaker unassigned successfully!");
    }

    // Method to send a notification
    private static void sendNotification() {
        clearScreen();
        System.out.print("Enter attendee ID to notify: ");
        int attendeeId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter session ID for the notification: ");
        int sessionId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter notification message: ");
        String message = scanner.nextLine();

        Attendee attendee = scheduleService.getAttendeeById(attendeeId);
        Session session = scheduleService.getSessionById(sessionId);

        if (attendee != null && session != null) {
            Notification notification = new Notification(0, message, new Date(), attendee, session);
            notificationService.addNotification(notification);
        } else {
            System.out.println("Invalid attendee or session ID.");
        }
    }

    // Method to modify a notification
    private static void modifyNotification() {
        clearScreen();
        System.out.print("Enter notification ID to modify: ");
        int notificationId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter new notification message: ");
        String newMessage = scanner.nextLine();
        notificationService.modifyNotification(notificationId, newMessage);
    }

    // Method to delete a notification
    private static void deleteNotification() {
        clearScreen();
        System.out.print("Enter notification ID to delete: ");
        int notificationId = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        notificationService.deleteNotification(notificationId);
    }

}
