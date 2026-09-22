package game2026;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CentralServer {

    static List<ClientHandlerThread> clients = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(9999);
        System.out.println("Serveren venter på client");

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Forbindelse forbundet");
            ClientHandlerThread client = new ClientHandlerThread(connectionSocket);
            clients.add(client);
            client.start();

        }
    }
    
    public static void sendMoveMessageToAll(String message) throws IOException {
        for (ClientHandlerThread client : clients) {
            client.sendMessage(message); //Hvis ikke sender client(altså den client som har rykket sig)
        }
    }
}