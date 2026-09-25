package game2026;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentralServer {

    static List<ClientHandlerThread> clients = new ArrayList<>();
    private static final Map<String, Position> positions = new HashMap<>();

    private static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(9999);
        System.out.println("Serveren venter på client");

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Forbindelse forbundet");
            ClientHandlerThread client = new ClientHandlerThread(connectionSocket);
            clients.add(client);
            System.out.println("---- client størrelse" + clients.size());
            client.start();

        }
    }
    public synchronized static void handleMoveRequest(String message, ClientHandlerThread sender) throws IOException {
        String[] parts = message.split(" ");
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        String direction = parts[3];
        String name = parts[4];
        int oldX = Integer.parseInt(parts[5]);
        int oldY = Integer.parseInt(parts[6]);

        Position current = positions.get(name);
        if (current == null) {
            current = new Position(oldX, oldY);
            positions.put(name, current);
        }

        String occupant = playerAt(x, y, name);
        if (occupant != null) {
            sendPointsToAll(name, 10);
            sendPointsToAll(occupant, -10);
            return;
        }

        current.x = x;
        current.y = y;
        sendPointsToAll(name, 1);
        sendMoveMessageToAll(String.format("MOVE %d %d %s %s", x, y, direction, name), sender);
    }

    private static String playerAt(int x, int y, String movingPlayer) {
        for (Map.Entry<String, Position> entry : positions.entrySet()) {
            if (!entry.getKey().equals(movingPlayer)
                    && entry.getValue().x == x && entry.getValue().y == y) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static void sendPointsToAll(String name, int delta) throws IOException {
        sendMoveMessageToAll(String.format("POINTS %s %d", name, delta), null);
    }

    public synchronized static void sendMoveMessageToAll(String message, ClientHandlerThread sender) throws IOException {
        for (ClientHandlerThread client : clients) {
            client.sendMessageToClient(message);
        }
    }
}