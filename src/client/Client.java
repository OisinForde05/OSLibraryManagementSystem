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

            String text;
            while (true) {
                System.out.print("Enter message (type 'exit' to quit): ");
                text = scanner.nextLine();
                if (text.equalsIgnoreCase("exit")) break;
                output.println(text);
                System.out.println("Server: " + input.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
