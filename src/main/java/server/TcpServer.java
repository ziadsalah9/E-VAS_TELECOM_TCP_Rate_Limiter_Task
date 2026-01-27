package server;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {

    public static int port = 9090;

    public static void main(String[] args) {

       // int noOfThreads = Runtime.getRuntime().availableProcessors();  // THIS FOR CPU PROCESSES LIKE MATH OPERATION
        int noOfThreads = 20 ; //  need a lot of threads in network cause thread wait a lot of its time waiting result from network (i/oBound).
        ExecutorService executorService = Executors.newFixedThreadPool(noOfThreads);
        try(ServerSocket serverSocket = new ServerSocket(port)){

            System.out.println("Server listening on port " + port);
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
