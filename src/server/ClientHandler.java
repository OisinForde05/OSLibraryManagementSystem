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

            output.println("Connected to server. Use REGISTER, LOGIN, CREATE_RECORD, GET_RECORDS, or EXIT.");

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
