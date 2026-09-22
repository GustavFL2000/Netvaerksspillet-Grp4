package game2026;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {

    DataOutputStream write;
    GUI gui;

    public Client(GUI gui) throws IOException {
        Socket clientSocket = new Socket("localhost", 9999);
        System.out.println("Forbindelse forbundet");
        this.gui = gui;

        write = new DataOutputStream(clientSocket.getOutputStream());

        (new RecieverThread(clientSocket, gui)).start();
    }

    public void movedMessage(int delta_x, int delta_y, String direction, String navn, int x, int y) throws IOException {
        write.writeBytes(String.format("MOVE %d %d %s %s %d %d%n",
                delta_x, delta_y, direction, navn, x, y));
    }
    
    public void pointMessage(String name, int points) throws IOException {
        write.writeBytes(String.format("POINTS %s %d%n", name, points));
    }

    public String[] splitSentence(String recievedMessage){
        return recievedMessage.split(" ");
    }

}