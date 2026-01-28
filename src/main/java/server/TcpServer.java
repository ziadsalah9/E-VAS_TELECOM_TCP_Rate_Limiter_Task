package server;

import client.configs.RateLimitConfig;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {


    public static void main(String[] args) {

       // int noOfThreads = Runtime.getRuntime().availableProcessors();  // THIS FOR CPU PROCESSES LIKE MATH OPERATION
      //  int noOfThreads = 20 ; //  need a lot of threads in network cause thread wait a lot of its time waiting result from network (i/oBound).
        ExecutorService executorService = Executors.newFixedThreadPool(RateLimitConfig.THREAD_POOL_SIZE);
        try(ServerSocket serverSocket = new ServerSocket(RateLimitConfig.SERVER_PORT)){

          //  System.out.println("Server listening on port " + port);

            System.out.println();
            System.out.println("=== Fixed Window Server Config ===");
            System.out.println("Server listening on port : " + RateLimitConfig.SERVER_PORT);
            System.out.println("RATE_LIMIT = " + RateLimitConfig.LIMIT);
            System.out.println("WINDOW_SECONDS = " + RateLimitConfig.WINDOW_SECONDS);
            System.out.println("THREAD_POOL_SIZE = " + RateLimitConfig.THREAD_POOL_SIZE);
            while(true){


                var clientSocket = serverSocket.accept();
                System.out.println("New client connected: " +
                        clientSocket.getInetAddress());

                executorService.submit(new ClientHandler(clientSocket));
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
