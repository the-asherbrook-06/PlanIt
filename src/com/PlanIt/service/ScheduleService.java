package com.PlanIt.service;

import com.PlanIt.models.Attendee;
import com.PlanIt.models.Session;
import com.PlanIt.models.Speaker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleService {

    // Add a Speaker to the Database
    public void assignSpeaker(Session session, Speaker speaker) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Session SET SpeakerID = ? WHERE SessionID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, speaker.getSpeakerId());
            stmt.setInt(2, session.getSessionId());
            stmt.executeUpdate();
            System.out.println("Speaker assigned to session successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifySpeaker(int speakerId, String newName, String newEmail) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Speaker SET Name = ?, Email = ? WHERE SpeakerID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newName);
            stmt.setString(2, newEmail);
            stmt.setInt(3, speakerId);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Speaker details updated successfully!");
            } else {
                System.out.println("Speaker not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get Speaker by ID
    public Speaker getSpeakerById(int speakerId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Speaker WHERE SpeakerID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, speakerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Speaker(
                        rs.getInt("SpeakerID"),
                        rs.getString("Name"),
                        rs.getString("Email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if not found
    }

    public void deleteSpeaker(int speakerId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM Speaker WHERE SpeakerID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, speakerId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Speaker deleted successfully!");
            } else {
                System.out.println("Speaker not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllSpeakers() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Speaker";
            PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();

            // Prepare to dynamically calculate column widths
            int maxSpeakerIdWidth = "ID".length();
            int maxNameWidth = "Name".length();
            int maxEmailWidth = "Email".length();

            // First pass: determine max column width based on data
            while (rs.next()) {
                int speakerId = rs.getInt("SpeakerID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");

                maxSpeakerIdWidth = Math.max(maxSpeakerIdWidth, String.valueOf(speakerId).length());
                maxNameWidth = Math.max(maxNameWidth, name.length());
                maxEmailWidth = Math.max(maxEmailWidth, email.length());
            }

            // Reset the ResultSet to iterate again
            rs.beforeFirst();

            // Print the table header with dynamic column width
            String headerFormat = "| %-"+maxSpeakerIdWidth+"s | %-"+maxNameWidth+"s | %-"+maxEmailWidth+"s |";
            System.out.printf(headerFormat, "ID", "Name", "Email");
            System.out.println();

            // Second pass: print the data rows
            while (rs.next()) {
                int speakerId = rs.getInt("SpeakerID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");

                // Print each row in tabular format with proper padding
                System.out.printf(headerFormat,
                        String.format("%" + maxSpeakerIdWidth + "d", speakerId),
                        name,
                        email);
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get Session by ID
    public Session getSessionById(int sessionId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Session WHERE SessionID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int speakerId = rs.getInt("SpeakerID");
                Speaker speaker = getSpeakerById(speakerId); // Get speaker details
                return new Session(
                        rs.getInt("SessionID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getTimestamp("SessionTime"),
                        speaker
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if not found
    }

    // Add a Session to the Database
    public void addSession(Session session) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Session (Title, Description, SessionTime, SpeakerID) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, session.getTitle());
            stmt.setString(2, session.getDescription());
            stmt.setTimestamp(3, new Timestamp(session.getSessionTime().getTime()));
            stmt.setInt(4, session.getSpeaker() != null ? session.getSpeaker().getSpeakerId() : 0);  // Handle null speaker
            stmt.executeUpdate();
            System.out.println("Session added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayUpcomingEvents(int numberOfEvents) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Session WHERE SessionTime > ? ORDER BY SessionTime ASC LIMIT ?";

            // Change ResultSet type to TYPE_SCROLL_INSENSITIVE to allow navigating back and forth
            PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // Get future sessions only
            stmt.setInt(2, numberOfEvents);
            ResultSet rs = stmt.executeQuery();

            // Prepare to dynamically calculate column widths
            int maxSessionIdWidth = "Session ID".length();
            int maxTitleWidth = "Title".length();
            int maxDescriptionWidth = "Description".length();
            int maxSessionTimeWidth = "Session Time".length();
            int maxSpeakerWidth = "Speaker".length();

            // First pass: determine max column width based on data
            while (rs.next()) {
                int sessionId = rs.getInt("SessionID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                Timestamp sessionTime = rs.getTimestamp("SessionTime");
                Speaker speaker = getSpeakerById(rs.getInt("SpeakerID")); // Get speaker for session

                maxSessionIdWidth = Math.max(maxSessionIdWidth, String.valueOf(sessionId).length());
                maxTitleWidth = Math.max(maxTitleWidth, title.length());
                maxDescriptionWidth = Math.max(maxDescriptionWidth, description.length());
                maxSessionTimeWidth = Math.max(maxSessionTimeWidth, sessionTime.toString().length());
                maxSpeakerWidth = Math.max(maxSpeakerWidth, (speaker != null ? speaker.getName() : "None").length());
            }

            // Reset the ResultSet to iterate again
            rs.beforeFirst();

            System.out.println();

            // Print the table header with dynamic column width
            String headerFormat = "| %-"+maxSessionIdWidth+"s | %-"+maxTitleWidth+"s | %-"+maxDescriptionWidth+"s | %-"+maxSessionTimeWidth+"s | %-"+maxSpeakerWidth+"s |";
            System.out.printf(headerFormat, "Session ID", "Title", "Description", "Session Time", "Speaker");
            System.out.println();

            // Second pass: print the data rows
            while (rs.next()) {
                int sessionId = rs.getInt("SessionID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                Timestamp sessionTime = rs.getTimestamp("SessionTime");
                Speaker speaker = getSpeakerById(rs.getInt("SpeakerID")); // Get speaker for session
                String speakerName = (speaker != null ? speaker.getName() : "None");

                // Print each row in tabular format with proper padding
                System.out.printf(headerFormat,
                        String.format("%" + maxSessionIdWidth + "d", sessionId), // Right-align session ID
                        title,
                        description,
                        sessionTime.toString(),
                        speakerName);
                System.out.println();
            }

            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Add an Attendee to the Database
    public void addAttendee(Attendee attendee) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Attendee (Name, Email) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, attendee.getName());
            stmt.setString(2, attendee.getEmail());
            stmt.executeUpdate();
            System.out.println("Attendee added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get Attendee by ID
    public Attendee getAttendeeById(int attendeeId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Attendee WHERE AttendeeID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, attendeeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Attendee(
                        rs.getInt("AttendeeID"),
                        rs.getString("Name"),
                        rs.getString("Email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if not found
    }

    // Register Attendee to a Session
    public void registerAttendee(int attendeeId, int sessionId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO AttendeeSession (AttendeeID, SessionID) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, attendeeId);
            stmt.setInt(2, sessionId);
            stmt.executeUpdate();
            System.out.println("Attendee registered to session successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Unregister Attendee from Session
    public void unregisterAttendee(int attendeeId, int sessionId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM AttendeeSession WHERE AttendeeID = ? AND SessionID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, attendeeId);
            stmt.setInt(2, sessionId);
            stmt.executeUpdate();
            System.out.println("Attendee unregistered from session successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayAllAttendees() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Attendee";
            PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery();

            // Prepare to dynamically calculate column widths
            int maxAttendeeIdWidth = "ID".length();
            int maxNameWidth = "Name".length();
            int maxEmailWidth = "Email".length();

            // First pass: determine max column width based on data
            while (rs.next()) {
                int attendeeId = rs.getInt("AttendeeID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");

                maxAttendeeIdWidth = Math.max(maxAttendeeIdWidth, String.valueOf(attendeeId).length());
                maxNameWidth = Math.max(maxNameWidth, name.length());
                maxEmailWidth = Math.max(maxEmailWidth, email.length());
            }

            // Reset the ResultSet to iterate again
            rs.beforeFirst();

            // Print the table header with dynamic column width
            String headerFormat = "| %-"+maxAttendeeIdWidth+"s | %-"+maxNameWidth+"s | %-"+maxEmailWidth+"s |";
            System.out.printf(headerFormat, "ID", "Name", "Email");
            System.out.println();

            // Second pass: print the data rows
            while (rs.next()) {
                int attendeeId = rs.getInt("AttendeeID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");

                // Print each row in tabular format with proper padding
                System.out.printf(headerFormat,
                        String.format("%" + maxAttendeeIdWidth + "d", attendeeId),
                        name,
                        email);
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyAttendee(int attendeeId, String newName, String newEmail) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Attendee SET Name = ?, Email = ? WHERE AttendeeID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newName);
            stmt.setString(2, newEmail);
            stmt.setInt(3, attendeeId);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Attendee details updated successfully!");
            } else {
                System.out.println("Attendee not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete an Attendee
    public void deleteAttendee(int attendeeId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM Attendee WHERE AttendeeID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, attendeeId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Attendee deleted successfully!");
            } else {
                System.out.println("Attendee not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Unassign Speaker from a Session
    public void unassignSpeaker(int sessionId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Session SET SpeakerID = NULL WHERE SessionID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sessionId);
            stmt.executeUpdate();
            System.out.println("Speaker unassigned from session successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
