package game2026;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecieverThread extends Thread{

    Socket conSocket;

    public RecieverThread(Socket conSocket) {
        this.conSocket = conSocket;
    }

    public void run() {
        String recievedSentence = "";

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conSocket.getInputStream())
            );

            while (true) {
                recievedSentence = reader.readLine();
                System.out.println(recievedSentence);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

