package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by DTG2 on 09/06/16.
 */
public class DrawingClient {


    public DrawingClient() {
    }

    public static void main(String[] args) {
        Scanner clientInput = new Scanner(System.in);


        try {
            Socket clientSocket = new Socket("localhost", 8005);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Let's begin");
            out.println("Let's Begin");
            System.out.println("ok ");
            String serverResponse = in.readLine();
            System.out.println(serverResponse);

            clientSocket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
