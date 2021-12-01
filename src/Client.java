import java.io.*;
import java.net.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {
    DataOutputStream outputToServer = null;
    DataInputStream inputFromServer = null;

    @Override
    public void start(Stage primaryStage) {
        // Panel p to hold the label and text field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter a number: "));

        TextField textField = new TextField();
        textField.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(textField);

        BorderPane mainPane = new BorderPane();

        // Text area to display contents
        TextArea textArea = new TextArea();
        mainPane.setCenter(new ScrollPane(textArea));
        mainPane.setTop(paneForTextField);

        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 450, 200);
        // Set the stage title
        primaryStage.setTitle("Client");
        // Place the scene in the stage
        primaryStage.setScene(scene);
        // Display the stage
        primaryStage.show();

        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create input stream to receive data from server
            inputFromServer = new DataInputStream(socket.getInputStream());

            // Create output stream to send data to the server
            outputToServer = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            textArea.appendText(e.toString() + "\n");
            e.printStackTrace();
        }

        textField.setOnAction(e -> {
            try {
                // Get the number from the textField
                int number = Integer.parseInt(textField.getText().trim());

                // Send the number to the server
                outputToServer.writeInt(number);
                outputToServer.flush();

                // Get result from the server
                String result = inputFromServer.readUTF();

                // Display to the text area
                textArea.appendText("The response received from the server is: " + result + "\n");
            } catch (IOException error) {
                System.err.println(error);
            }
        });
    } // End of start method

    public static void main(String[] args) {
        launch(args);
    } // End of main method
} // End of class Client


