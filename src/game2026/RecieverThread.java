package game2026;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecieverThread extends Thread{

    Socket conSocket;
    GUI gui;

    public RecieverThread(Socket conSocket, GUI gui) {
        this.conSocket = conSocket;
        this.gui = gui;
    }

    public void run() {
        String recievedSentence = "";

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conSocket.getInputStream())
            );

            while (true) {
                recievedSentence = reader.readLine();

                String[] parts = gui.client.splitSentence(recievedSentence);

                if (parts[0].equals("MOVE")) {
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    String direction = parts[3];
                    String navn = parts[4];
                    
                    if (!gui.getScoreList().contains(navn)) {
                        gui.addPlayer(navn, x, y, direction);
                    }
                    
                    int delta_x = Integer.parseInt(parts[5]);
                    int delta_y = Integer.parseInt(parts[6]);

                    Platform.runLater(() -> {
                        gui.moveOtherPlayer(x, y, direction, navn);
                    });
                }
                if(parts[0].equals("POINTS")){
                    String navn = parts[1];
                    int points = Integer.parseInt(parts[2]);

                    Platform.runLater(() ->{
                        gui.updatePoints(navn,points);
                    });
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

