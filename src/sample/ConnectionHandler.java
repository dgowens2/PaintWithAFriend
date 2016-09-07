package sample;

import javafx.scene.canvas.GraphicsContext;
import jodd.json.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.System.out;

/**
 * Created by DTG2 on 09/04/16.
 */
public class ConnectionHandler implements Runnable{

    Socket connection;
    GraphicsContext gc;

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

        try {
            String input;
            while ((input = inputFromClient.readLine()) != null) {
                System.out.println(input);
                outputToClient.println("Output Test");
                System.out.println(input);
//                Stroke currentStroke = jsonDeserialize(input);
//                System.out.println(currentStroke.toString());
//                    jsonDeserialize(input);
//                gc.strokeOval(currentStroke.getxPlane(), currentStroke.getyPlane(), currentStroke.getStrokeSize(), currentStroke.getStrokeSize());

//            Platform.runLater(new RunnableGC(gc, jsonRestored));

            }


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Stroke jsonDeserialize(String jsonString) {
        JsonParser ToDoItemParser = new JsonParser();
        Stroke strokeItem = ToDoItemParser.parse(jsonString, Stroke.class);

        out.println("Deserialize " + strokeItem);

        return strokeItem;
    }
}
