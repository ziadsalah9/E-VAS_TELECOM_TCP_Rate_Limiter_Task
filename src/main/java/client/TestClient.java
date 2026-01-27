package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class TestClient {

    public static void main(String[] args) throws IOException {


    Socket socket = new Socket("localhost", 9090);

    BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));

    PrintWriter out = new PrintWriter(
            socket.getOutputStream(), true);

    for (int i = 1; i <= 15; i++) {
        out.println("REQUEST");
        String response = in.readLine();

        System.out.println("Server Response: " + response);
    }
        socket.close();
}
}
