package game2026;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    DataOutputStream write;

    public Client() throws IOException {
        Socket clientSocket = new Socket("10.10.139.60", 9999);
        System.out.println("Forbindelse forbundet");

        write = new DataOutputStream(clientSocket.getOutputStream());

        (new RecieverThread(clientSocket)).start();
    }

    public void movedMessage(int x, int y, String direction) throws IOException {
        write.writeBytes(String.format("MOVE %d %d %s%n", x, y, direction));
    }
    
    public void pointMessage(String name, int points) throws IOException {
        write.writeBytes(String.format("POINTS %s %d", name, points));
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }
}