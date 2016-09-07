package sample;

import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by DTG2 on 09/04/16.
 */
public class Server implements Runnable{
    GraphicsContext gc = null;

    public void run() {
        startServer();
    }

    public Server(GraphicsContext gc) {
        this.gc = gc;
    }

    public void startServer() {
        try {
            System.out.println("Server opening...");
            ServerSocket serverListener = new ServerSocket(8005);
            System.out.println("The audience is listening...");

            while (true) {
                Socket incomingConnection = serverListener.accept();
                System.out.println("Connection open.");
                ConnectionHandler handler = new ConnectionHandler(incomingConnection, gc);
                Thread handlerThread = new Thread(handler);
                handlerThread.start();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
