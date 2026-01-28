package client.slidingWindow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class testSlidingWindowClient {


    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 9091);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );

        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true
        );

        System.out.println("Connected to Sliding Window Server");

        for (int i = 1; i <= 15; i++) {
            out.println("REQUEST");
            String response = in.readLine();
            System.out.println("Request " + i + ": " + response);
            Thread.sleep(200); // simulate traffic
        }

        System.out.println("Waiting 10 seconds...");
        Thread.sleep(10000);

        for (int i = 1; i <= 15; i++) {
            out.println("REQUEST");
            Thread.sleep(5000);

            System.out.println("After wait " + i + ": " + in.readLine());

        }

        socket.close();
    }
}
