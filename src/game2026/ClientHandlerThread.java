package game2026;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientHandlerThread extends Thread {
    private Socket socket;
    
    private List<Player> tilmeldt;
    
    public ClientHandlerThread(Socket socket, List<Player> tilmeldt) {
        this.socket = socket;
        this.tilmeldt = tilmeldt;
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
                System.out.println(message);
                write.writeBytes(message + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
