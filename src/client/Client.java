package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println(input.readLine());

            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();
                output.println(command);
                String response = input.readLine();
                if (response == null) break;
                System.out.println(response);
                if (command.equalsIgnoreCase("EXIT")) break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
