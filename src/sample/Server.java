package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by DTG2 on 09/04/16.
 */
public class Server implements Runnable{
    public void run() {
    }

    public void startServer() {
        try {
            System.out.println("Server opening...");
            ServerSocket serverListener = new ServerSocket(8005);
            System.out.println("The audience is listening...");

            while (true) {
                Socket incomingConnection = serverListener.accept();
                System.out.println("Connection open.");
                ConnectionHandler handler = new ConnectionHandler(incomingConnection);
                Thread handlerThread = new Thread(handler);
                handlerThread.start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server myServer = new Server();
        try{
            myServer.startServer();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
