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

            output.println("Connected. Commands: REGISTER, LOGIN, CREATE_RECORD, GET_RECORDS, ASSIGN_RECORD, UPDATE_STATUS, UPDATE_PASSWORD, EXIT");

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
                        } else {
                            output.println("Invalid email or password");
                        }
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
                        String recordType = parts[1];
                        LibraryRecord record = RecordDatabase.createRecord(recordType, loggedInUser.getStudentId(), "Available");
                        output.println("Record created: " + record);
                        break;

                    case "GET_RECORDS":
                        if (loggedInUser == null) {
                            output.println("You must log in first.");
                            break;
                        }
                        for (LibraryRecord r : RecordDatabase.getAllRecords()) {
                            output.println(r.toString());
                        }
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
                            String librarianId = parts[2];
                            boolean assigned = RecordDatabase.assignRecord(recordId, librarianId);
                            output.println(assigned ? "Record assigned." : "Record not found.");
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
                            String status = parts[2];
                            boolean updated = RecordDatabase.updateStatus(recordId, status);
                            output.println(updated ? "Status updated." : "Record not found.");
                        } catch (NumberFormatException e) {
                            output.println("Invalid record ID.");
                        }
                        break;

                    case "EXIT":
                        output.println("Goodbye");
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
