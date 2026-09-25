package game2026;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandlerThread extends Thread {
    private final Socket socket;
    private DataOutputStream write;
    
    public ClientHandlerThread(Socket socket) throws IOException {
        this.socket = socket;
        this.write = new DataOutputStream(socket.getOutputStream());
        socket.setTcpNoDelay(true);
    }

    public void sendMessageToClient(String message) throws IOException {
        write.writeBytes(message + "\n");
    }
    
    @Override
    public void run() {
        BufferedReader reader;
        String message;
        
        try {
            reader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            
            while (true) {
                InetAddress socketID = socket.getInetAddress();
                message = reader.readLine();
                System.out.println(message); //Kan ud kommenteres så printer sevrer consollen ikke beskederbne
                if (message.startsWith("MOVE_REQUEST")) {
                    CentralServer.handleMoveRequest(message, this);
                }
                if (message.startsWith("POINTS")) {
                    CentralServer.sendMoveMessageToAll(message,this);
                }

                //write.writeBytes(message + "\n"); //brugt til da vi kun skulle echo tilbage
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}