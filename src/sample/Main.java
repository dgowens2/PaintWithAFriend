package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jodd.json.JsonSerializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Main extends Application {

    final double DEFAULT_SCENE_WIDTH = 800;
    final double DEFAULT_SCENE_HEIGHT = 600;
    boolean mouseMoving = true;
    double X;
    double Y;
    double strokeSize = 5;
    GraphicsContext gc = null;
    GraphicsContext gcSecond = null;
    Stroke strokeInfo ;
    PrintWriter outputToServer;
    BufferedReader inputFromServer;
    boolean drawingClient = false;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Client's World");

//      this uses a grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);//gap between rows
        grid.setVgap(10);//gap between columns
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(true);

        // add buttons and canvas to the grid
        Text sceneTitle = new Text("Welcome to Donald's Paint Application");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0); //sets to where you want to add this info to the grid. 1st column, then row

        Button buttonOne = new Button("Open second screen");
        HBox hbButton = new HBox(10); //create container for the button
        hbButton.setAlignment(Pos.TOP_LEFT); //set container alignment
        hbButton.getChildren().add(buttonOne); //add button to the container
        grid.add(hbButton, 0, 1);

        Button buttonTwo = new Button("Open client");
        hbButton.getChildren().add(buttonTwo);

        Button buttonThree = new Button("Start Server");
        hbButton.getChildren().add(buttonThree);

        buttonOne.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                outputToServer.println("I can switch to another scene here ...");
                startSecondStage();
            }
        });

        buttonTwo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                drawingClient();
                drawingClient = true;
            }
        });

        buttonThree.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
            public void handle(ActionEvent e) {
                startServer();
            }
        });

        // add canvas
        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT-100); //canvas matches the scene width but is less than the screen height

        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
        gc.setLineWidth(strokeSize);

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {//setup mouse event
//        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {//alternate setup mouse event

            @Override
            public void handle(MouseEvent e) {
              if (mouseMoving) {
                  if (e.isDragDetect()) {
                      gc.strokeOval(e.getX(), e.getY(), strokeSize, strokeSize);
                      strokeInfo = new Stroke(e.getX(),e.getY(),strokeSize);

                      jsonSerialize(strokeInfo);
                      if (drawingClient == true) {
                          outputToServer.println(jsonSerialize(strokeInfo));
                      }

                  }
                  if(gcSecond != null) {
                      gcSecond.strokeOval(strokeInfo.getxPlane(), strokeInfo.yPlane, strokeInfo.getStrokeSize(), strokeInfo.getStrokeSize());

                  }
              }
            }
        });

        grid.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent e) {
                if (e.getText().equalsIgnoreCase("c")) {
                    try {
                        start(primaryStage);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
//                outputToServer.println(e.getCode());
//                outputToServer.println(e.getText());
                } else if (e.getCode() == KeyCode.UP) {
                    strokeSize += 10;
                    if (strokeSize == 21) {
                        strokeSize -=1;
                    }
                } else if (e.getCode() == KeyCode.DOWN) {
                    strokeSize -= 10;
                    if (strokeSize == 1) {
                        strokeSize +=1;
                    }
                } else if (e.getText().equalsIgnoreCase("d")){
                    mouseMoving = !mouseMoving;
                }
            }
        });

        grid.add(canvas, 0, 2);

//      set our grid scene layout on the scene
        Scene defaultScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

//      sets the stage
        primaryStage.setScene(defaultScene);//removed original settings and replaced with default scene
        primaryStage.show();
    }

    public void startSecondStage() {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Server's World");

        // we're using a grid layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setGridLinesVisible(false);
//        grid.setPrefSize(primaryStage.getMaxWidth(), primaryStage.getMaxHeight());

        // add buttons and canvas to the grid
        Text sceneTitle = new Text("Welcome to Paint application");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0);

        Button button = new Button("Start client");
        HBox hbButton = new HBox(10);
        hbButton.setAlignment(Pos.TOP_LEFT);
        hbButton.getChildren().add(button);
        grid.add(hbButton, 0, 1);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                outputToServer.println("I can switch to another scene here ...");
            }
        });

        // add canvas
        Canvas canvas= new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT-100);

        gcSecond = canvas.getGraphicsContext2D();

        grid.add(canvas, 0, 2);

        // set our grid layout on the scene
        Scene defaultScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

        secondaryStage.setScene(defaultScene);

        secondaryStage.show();
    }

    public static void startServer() {
        Server myServer = new Server();
        Thread serverThread = new Thread(myServer);
        serverThread.start();
    }

    public void drawingClient() {

//        if (drawingClient == true) {
            try {
                Socket clientSocket = new Socket("localhost", 8005);
                outputToServer = new PrintWriter(clientSocket.getOutputStream(), true);
                inputFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                outputToServer.println("Let's begin");
                String serverResponse = inputFromServer.readLine();
                System.out.println(serverResponse);

//            clientSocket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
//        }
    }

    public String jsonSerialize(Stroke strokeInfo) {
        JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
        String jsonString = jsonSerializer.serialize(strokeInfo);

//        System.out.println("Serialize test " + jsonString);

        return jsonString;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
