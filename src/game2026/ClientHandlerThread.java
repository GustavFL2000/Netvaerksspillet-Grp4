package game2026;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class ClientHandlerThread extends Thread {
    private Socket socket;
    
    public ClientHandlerThread(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(String message) throws IOException {
        DataOutputStream write = new DataOutputStream(socket.getOutputStream());
        write.writeBytes(message + "\n");
    }
    
    @Override
    public void run() {
        BufferedReader reader;
        DataOutputStream write;
        String message = null;
        
        try {
            reader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            write = new DataOutputStream(socket.getOutputStream());
            
            while (true) {
                InetAddress socketID = socket.getInetAddress();
                message = reader.readLine();
                System.out.println(socketID+ ": " + message); //Kan ud kommenteres så printer sevrer consollen ikke beskederbne
                if (message.contains("MOVE")) {
                    CentralServer.sendMoveMessageToAll(socketID+ ": " + message);   // skal have beskeden echoet tilbage kan vi lave et if client != sender eller sådan
                } else if (message.contains("POINT")) {
                    CentralServer.sendMoveMessageToAll(socketID+ ": " + message);
                }

                //write.writeBytes(message + "\n"); //brugt til da vi kun skulle echo tilbage
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}