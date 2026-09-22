package game2026;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

    DataOutputStream write;

    public Client() throws IOException {
        Socket clientSocket = new Socket("localhost", 9999);
        System.out.println("Forbindelse forbundet");

        write = new DataOutputStream(clientSocket.getOutputStream());

        (new RecieverThread(clientSocket)).start();

    }

    public void movedMessage(int x, int y, String direction) throws IOException {
        write.writeBytes(String.format("MOVE %d %d %s%n", x, y, direction));
    }
    
    public void pointMessage(String name, int points) throws IOException {
        write.writeBytes(String.format("POINTS %s %d%n", name, points));
    }

    public String[] splitSentence(String recievedMessage){
        return recievedMessage.split(" ");
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }
}