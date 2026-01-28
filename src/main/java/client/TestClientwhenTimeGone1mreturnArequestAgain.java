package client;

import configs.RateLimitConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClientwhenTimeGone1mreturnArequestAgain {

    public static void main(String[] args) {

        String host = "localhost";
       // int port = 9090;

        try (Socket socket = new Socket(host, RateLimitConfig.SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {

            System.out.println("=== Sending first 12 requests ===");
            for (int i = 1; i <= 12; i++) {
                out.println("REQUEST");
                System.out.println("Request " + i + ": " + in.readLine());
            }

            System.out.println();
            System.out.println("=== Waiting for window reset (65 seconds) ===");
            Thread.sleep(65000); // wait 65 seconds

            System.out.println();
            System.out.println("=== Sending 3 requests after window reset ===");
            for (int i = 1; i <= 3; i++) {
                out.println("REQUEST");
                System.out.println("After reset " + i + ": " + in.readLine());
            }

            System.out.println();
            System.out.println("Test finished.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
