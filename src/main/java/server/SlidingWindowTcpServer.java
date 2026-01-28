package server;

import Repository.RedisRateLimitRepository;
import client.configs.RateLimitConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SlidingWindowTcpServer {



    public static void main(String[] args) throws IOException {

        var serverSocket = new ServerSocket(RateLimitConfig.SLIDING_WINDOW_SERVER_PORT);

        ExecutorService executor = Executors.newFixedThreadPool(RateLimitConfig.SLIDING_WINDOW_THREAD_POOL_SIZE);

        System.out.println();
        System.out.println("=== Sliding Window Server Config ===");
        System.out.println("PORT = " + RateLimitConfig.SLIDING_WINDOW_SERVER_PORT);
        System.out.println("RATE_LIMIT = " + RateLimitConfig.LIMIT);
        System.out.println("WINDOW_SECONDS = " + RateLimitConfig.WINDOW_SECONDS);
        System.out.println("THREAD_POOL_SIZE = " + RateLimitConfig.SLIDING_WINDOW_THREAD_POOL_SIZE);

        RedisRateLimitRepository repository = new RedisRateLimitRepository();


        while (true) {
            Socket socket = serverSocket.accept();
            executor.submit(() -> handleClient(socket, repository));

        }

    }

    private static void handleClient(Socket socket, RedisRateLimitRepository repository) {


        String clientId = socket.getRemoteSocketAddress().toString();



        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        )
        {

            System.out.println("Client connected (SW): " + clientId);

            String line;

            while ((line = in.readLine()) != null) {

                if (!line.equals("REQUEST")) {
                    out.println("INVALID");
                    continue;
                }

                boolean allowed = repository.allowRequestSliding(clientId);
                out.println(allowed ? "ALLOW" : "DENY");
            }
        }catch (Exception e) {
            System.out.println("Client disconnected (SW): " + clientId);
        }

    }



}
