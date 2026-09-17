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
        try {
            reader = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DataOutputStream write;
        try {
            write = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String message = null;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(message);

        try {
            write.writeBytes(message + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
