package server;

import Repository.RedisRateLimitRepository;
import service.RateLimitService;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;

    private static final RedisRateLimitRepository repository = new RedisRateLimitRepository();
    private static final RateLimitService rateLimitService = new RateLimitService(repository);
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

try (
    BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));


        var out = new PrintWriter(socket.getOutputStream(),true);
        ){

    String input ;
    String clientId = socket.getInetAddress().getHostAddress();


    while ((input=in.readLine())!=null){


        if (!input.equalsIgnoreCase("REQUEST")) {
            out.println("INVALID");
            continue;
        }

        var result = rateLimitService.checkRateLimitResult(clientId);
        out.println(result.name());

//        System.out.print("Received from client : "+input);
//
//        out.println("client Received from server  : "+input);
    }

            }
catch (Exception e){
    e.printStackTrace();
}
finally {

    try {
        socket.close();
        System.out.println("Client disconnected");

    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}


    }
}
