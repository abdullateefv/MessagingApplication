package MessagingApplication;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Objects;

public class Main extends Application {

    //Instantiates view resource file & stage variables
    public static Stage primaryStage;

    //Creates JDBC SQL Server connection object
    public static Connection conn;

    //Local copy of messages
    public static ObservableList<String> localMessages = FXCollections.observableArrayList();

    //Stores client information
    public static String sessionUsername;
    public static String sessionUser;
    public static String sessionUserMessageColor;

    //Program Variable
    public static Boolean flag = false;

    //Launches JavaFX
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    //Entry point to application
    public void start(Stage primaryStage) throws IOException {

        Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));

        //Allows for primaryStage access in all files
        Main.primaryStage = primaryStage;

        //Sets up login scene
        primaryStage.setTitle("Messaging");
        Scene scene = new Scene(loginRoot, 300, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

