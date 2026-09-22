package game2026;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientHandlerThread extends Thread {
    private Socket socket;

    private List<ClientHandlerThread> clients;
    
    public ClientHandlerThread(Socket socket, List<ClientHandlerThread> clients) {
        this.socket = socket;
        this.clients = clients;
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
                message = reader.readLine();
                System.out.println(message); //Kan ud kommenteres så printer sevrer consollen ikke beskederbne
                for (ClientHandlerThread client : clients) {
                    client.sendMessage(message); //Hvis ikke sender client(altså den client som har rykket sig )
                }                                 // skal have beskeden echoet tilbage kan vi lave et if client != sender eller sådan

                //write.writeBytes(message + "\n"); //brugt til da vi kun skulle echo tilbage
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
