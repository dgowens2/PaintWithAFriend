package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by DTG2 on 09/04/16.
 */
public class ConnectionHandler implements Runnable{

    Socket connection;

    public void run(){
        try{
            handleIncomingConnection(connection);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public ConnectionHandler(Socket connection) {
        this.connection = connection;
    }

    private void handleIncomingConnection (Socket clientSocket) throws IOException {

        System.out.println("Incoming connection to server from clientSocket: " + clientSocket.getInetAddress().getHostAddress());

        BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter outputToClient = new PrintWriter(clientSocket.getOutputStream(), true);

        outputToClient.println("What is your name?");

        try {
            String userName;
            if ((userName = inputFromClient.readLine()) != null) {
                System.out.println(userName);
                conversationHandler(outputToClient, inputFromClient);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void conversationHandler(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException {
        Scanner serverInput = new Scanner(System.in);
        String clientText;
        String serverText;

        while (true) {

            if ((serverText = serverInput.nextLine()) != null) {
                outputToClient.println(serverText);
                clientText = inputFromClient.readLine();
                System.out.println(clientText);
            }
        }
    }
}
