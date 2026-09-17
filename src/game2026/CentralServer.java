package game2026;

import java.net.ServerSocket;
import java.net.Socket;

public class CentralServer {
    public static void main(String[] args) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(9999);
        System.out.println("Serveren venter på client ");
        Socket connectionSocket = welcomeSocket.accept();
        System.out.println("Forbindelse forbundet");
        (new RecieverThread(connectionSocket)).start();
        (new SenderThread(connectionSocket)).start();
    }
}
