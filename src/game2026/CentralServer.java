package game2026;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CentralServer {

    static List<Player> tilmeldt = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(9999);

        System.out.println("Serveren venter på client");

        while (true) {

            Socket connectionSocket = welcomeSocket.accept();

            System.out.println("Forbindelse forbundet");

            new ClientHandlerThread(connectionSocket, tilmeldt).start();
        }
    }
}