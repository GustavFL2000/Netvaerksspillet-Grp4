package game2026;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    DataOutputStream write;
    GUI gui;

    public Client(GUI gui) throws IOException {
        Socket clientSocket = new Socket("localhost", 9999);
        clientSocket.setTcpNoDelay(true);
        System.out.println("Forbindelse forbundet");
        this.gui = gui;

        write = new DataOutputStream(clientSocket.getOutputStream());

        (new RecieverThread(clientSocket, gui)).start();
    }

    public void movedMessage(int x, int y, String direction, String navn, int delta_x, int delta_y) throws IOException {
        write.writeBytes(String.format("MOVE_REQUEST %d %d %s %s %d %d%n",
                x, y, direction, navn, delta_x, delta_y));
    }
    
    public void pointMessage(String name, int points) throws IOException {
        write.writeBytes(String.format("POINTS %s %d%n", name, points));
    }

    public String[] splitSentence(String recievedMessage){
        return recievedMessage.split(" ");
    }

}