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
import java.sql.Statement;
import java.util.Objects;

public class Main extends Application {
    //
    public static Parent root2;
    static {
        try {
            root2 = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Messaging.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Creates JDBC SQL Server connection object
    public static Connection conn = Queries.getConnection();

    //Local copy of messages
    public static ObservableList<String> localMessages = FXCollections.observableArrayList();

    //Stores client information
    public static String sessionUserAccountID;
    public static String sessionUsername;
    public static String sessionUser;
    public static String sessionUserMessageColor;

    //Program Variables
    public static Boolean flag = false;
    public static Stage primaryStage;

    //Launches JavaFX
    public static void main(String[] args) {
        launch(args);
    }

    //Entry point to application, handles login screen logic
    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        //Sets up login scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        primaryStage.setTitle("Messaging");
        Scene scene = new Scene(root, 300, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Executes when chat window is closed, clears chat history from SQL Server
    @Override
    public void stop() throws Exception {
        Statement statement = conn.createStatement();
        statement.executeUpdate("TRUNCATE TABLE ChatApp.dbo.messages");
    }

}

