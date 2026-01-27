package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestConcurrentClients {

    private static final String HOST = "localhost";
    private static final int PORT = 9090;
    private static final int REQUESTS_PER_CLIENT = 12;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 3  clients
        for (int clientNum = 1; clientNum <= 3; clientNum++) {
            final int clientId = clientNum;

            executor.submit(() -> runClient(clientId));
        }

        executor.shutdown();
    }

    private static void runClient(int clientId) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Client " + clientId + " connected.");

            for (int i = 1; i <= REQUESTS_PER_CLIENT; i++) {
                out.println("REQUEST");
                String response = in.readLine();
                System.out.println("Client " + clientId + " request " + i + ": " + response);
            }

            if (clientId == 1) { // only one client waits
                System.out.println("Client " + clientId + " waiting 65 seconds for window reset...");
                Thread.sleep(65000);

                for (int i = 1; i <= 3; i++) {
                    out.println("REQUEST");
                    String response = in.readLine();
                    System.out.println("Client " + clientId + " after reset " + i + ": " + response);
                }
            }

            System.out.println("Client " + clientId + " finished.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
