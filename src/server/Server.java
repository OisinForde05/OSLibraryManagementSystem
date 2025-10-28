package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import utils.*;

public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        UserDatabase.loadFromFile();
        RecordDatabase.loadFromFile();
        LoggerUtil.log("Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                LoggerUtil.log("Client connected: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
