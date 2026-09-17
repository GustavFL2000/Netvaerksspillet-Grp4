package game2026;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    DataOutputStream write;

    public Client() throws IOException {
        Socket clientSocket = new Socket("localhost", 9999);
        System.out.println("Forbindelse forbundet");

        write = new DataOutputStream(clientSocket.getOutputStream());

        (new RecieverThread(clientSocket)).start();
    }

    public void movedMessage() throws IOException {
        write.writeBytes("Der er flyttet\n");
    }

    public static void main(String[] args) throws Exception {
        new Client();
    }
}