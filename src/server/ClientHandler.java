package server;

import java.io.*;
import java.net.*;
import models.*;
import utils.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private User loggedInUser;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            output.println("Connected. Commands: REGISTER, LOGIN, CREATE_RECORD, GET_RECORDS, ASSIGN_RECORD, UPDATE_STATUS, UPDATE_PASSWORD, VIEW_ASSIGNED, EXIT");

            String message;
            while ((message = input.readLine()) != null) {
                String[] parts = message.split(" ");
                String command = parts[0].toUpperCase();

                switch (command) {
                    case "REGISTER":
                        if (parts.length < 7) {
                            output.println("Usage: REGISTER name studentId email password department role");
                            break;
                        }
                        if (parts[4].length() < 4) {
                            output.println("Password too short.");
                            break;
                        }
                        User newUser = new User(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                        boolean registered = UserDatabase.register(newUser);
                        output.println(registered ? "Registration successful" : "Email or Student ID already exists");
                        if (registered) LoggerUtil.log("New user registered: " + parts[3]);
                        break;

                    case "LOGIN":
                        if (parts.length < 3) {
                            output.println("Usage: LOGIN email password");
                            break;
                        }
                        User user = UserDatabase.login(parts[1], parts[2]);
                        if (user != null) {
                            loggedInUser = user;
                            output.println("Login successful as " + user.getRole() + ": " + user.getName());
                            LoggerUtil.log("User logged in: " + user.getEmail());
                        } else output.println("Invalid email or password");
                        break;

                    case "UPDATE_PASSWORD":
                        if (loggedInUser == null) {
                            output.println("You must log in first.");
                            break;
                        }
                        if (parts.length < 2) {
                            output.println("Usage: UPDATE_PASSWORD newPassword");
                            break;
                        }
                        boolean changed = UserDatabase.updatePassword(loggedInUser, parts[1]);
                        output.println(changed ? "Password updated." : "Invalid password.");
                        if (changed) LoggerUtil.log("Password updated for: " + loggedInUser.getEmail());
                        break;

                    case "CREATE_RECORD":
                        if (loggedInUser == null) {
                            output.println("You must log in first.");
                            break;
                        }
                        if (parts.length < 2) {
                            output.println("Usage: CREATE_RECORD recordType");
                            break;
                        }
                        LibraryRecord record = RecordDatabase.createRecord(parts[1], loggedInUser.getStudentId(), "Available");
                        output.println("Record created: " + record);
                        LoggerUtil.log("Record created by " + loggedInUser.getEmail());
                        break;

                    case "GET_RECORDS":
                        if (loggedInUser == null) {
                            output.println("You must log in first.");
                            break;
                        }
                        for (LibraryRecord r : RecordDatabase.getAllRecords()) output.println(r.toString());
                        output.println("END_OF_RECORDS");
                        break;

                    case "ASSIGN_RECORD":
                        if (loggedInUser == null || !loggedInUser.getRole().equalsIgnoreCase("Librarian")) {
                            output.println("Only librarians can assign records.");
                            break;
                        }
                        if (parts.length < 3) {
                            output.println("Usage: ASSIGN_RECORD recordId librarianId");
                            break;
                        }
                        try {
                            int recordId = Integer.parseInt(parts[1]);
                            boolean assigned = RecordDatabase.assignRecord(recordId, parts[2]);
                            output.println(assigned ? "Record assigned." : "Record not found.");
                            if (assigned) LoggerUtil.log("Record " + recordId + " assigned by " + loggedInUser.getEmail());
                        } catch (NumberFormatException e) {
                            output.println("Invalid record ID.");
                        }
                        break;

                    case "UPDATE_STATUS":
                        if (loggedInUser == null) {
                            output.println("You must log in first.");
                            break;
                        }
                        if (parts.length < 3) {
                            output.println("Usage: UPDATE_STATUS recordId newStatus");
                            break;
                        }
                        try {
                            int recordId = Integer.parseInt(parts[1]);
                            boolean updated = RecordDatabase.updateStatus(recordId, parts[2]);
                            output.println(updated ? "Status updated." : "Record not found.");
                            if (updated) LoggerUtil.log("Record " + recordId + " status changed to " + parts[2]);
                        } catch (NumberFormatException e) {
                            output.println("Invalid record ID.");
                        }
                        break;

                    case "VIEW_ASSIGNED":
                        if (loggedInUser == null || !loggedInUser.getRole().equalsIgnoreCase("Librarian")) {
                            output.println("Only librarians can view assigned records.");
                            break;
                        }
                        for (LibraryRecord r : RecordDatabase.getRecordsByLibrarian(loggedInUser.getStudentId()))
                            output.println(r.toString());
                        output.println("END_OF_RECORDS");
                        break;

                    case "EXIT":
                        output.println("Goodbye");
                        LoggerUtil.log("Client disconnected: " + (loggedInUser != null ? loggedInUser.getEmail() : "unknown"));
                        clientSocket.close();
                        return;

                    default:
                        output.println("Unknown command");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
