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
    GraphicsContext gcSecond = null;
    Server myServer = new Server();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Alex's World");

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

        Button buttonOne = new Button("Sample paint button");
        HBox hbButton = new HBox(10); //create container for the button
        hbButton.setAlignment(Pos.TOP_LEFT); //set container alignment
        hbButton.getChildren().add(buttonOne); //add button to the container
        grid.add(hbButton, 0, 1);

        Button buttonTwo = new Button("Open client");
        hbButton.getChildren().add(buttonTwo);

        buttonOne.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("I can switch to another scene here ...");
                startSecondStage();
//                myServer.startServer();
            }
        });

        buttonTwo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });

        // add canvas
        Canvas canvas = new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT-100); //canvas matches the scene width but is less than the screen height

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
        gc.setLineWidth(5);

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {//setup mouse event
//        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {//alternate setup mouse event

            @Override
            public void handle(MouseEvent e) {
              if (mouseMoving) {
                  if (e.isDragDetect()) {
                      gc.strokeOval(e.getX(), e.getY(), 10, 10);
                      X = e.getX();
                      Y = e.getY();
                  }
                  if(gcSecond != null) {
                      gcSecond.strokeOval(X, Y, 10, 10);
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
                System.out.println(e.getCode());
                System.out.println(e.getText());
                } else if (e.getCode() == KeyCode.UP) {
                    gc.setLineWidth(gc.getLineWidth() + 10);
                } else if (e.getCode() == KeyCode.DOWN) {
                    gc.setLineWidth(gc.getLineWidth() - 10);
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
        secondaryStage.setTitle("Audrey's World");

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

//        Button button = new Button("Sample paint button");
//        HBox hbButton = new HBox(10);
//        hbButton.setAlignment(Pos.TOP_LEFT);
//        hbButton.getChildren().add(button);
//        grid.add(hbButton, 0, 1);
//
//        button.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//                System.out.println("I can switch to another scene here ...");
//            }
//        });

        // add canvas
        Canvas canvas= new Canvas(DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT-100);

        gcSecond = canvas.getGraphicsContext2D();

        grid.add(canvas, 0, 2);

        // set our grid layout on the scene
        Scene defaultScene = new Scene(grid, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

        secondaryStage.setScene(defaultScene);
//        System.out.println("About to show the second stage");

        secondaryStage.show();

    }

    public static void drawingClient() {

        try {
        Socket clientSocket = new Socket("localhost", 8005);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String serverResponse;
        serverResponse = in.readLine();
        System.out.println("Received message: " + serverResponse);


        clientSocket.close();
        } catch (IOException exception) {
        exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
