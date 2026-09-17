package game2026;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CentralServer {

    public static void main(String[] args) throws Exception {

        ServerSocket welcomeSocket = new ServerSocket(9999);

        System.out.println("Serveren venter på client");

        Socket connectionSocket = welcomeSocket.accept();

        System.out.println("Forbindelse forbundet");

        BufferedReader reader = new BufferedReader(
                new java.io.InputStreamReader(connectionSocket.getInputStream()));

        DataOutputStream write = new DataOutputStream(
                connectionSocket.getOutputStream());

        while (true) {
            String message = reader.readLine();

            System.out.println(message);

            write.writeBytes(message + "\n");
        }
    }
}