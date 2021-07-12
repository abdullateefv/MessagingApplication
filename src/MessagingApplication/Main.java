package MessagingApplication;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;
import java.util.Objects;

public class Main extends Application {

    //Instantiates view resource file & stage variables
    public static Parent messagingRoot;
    public static Parent loginRoot;
    public static Stage primaryStage;

    //Creates JDBC SQL Server connection object
    public static Connection conn = Queries.getConnection();

    //Local copy of messages
    public static ObservableList<String> localMessages = FXCollections.observableArrayList();

    //Stores client information
    public static String sessionUserAccountID;
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
    //Entry point to application, handles login screen logic
    public void start(Stage primaryStage) throws Exception {
        //Initializes stage & view resource file
        Main.primaryStage = primaryStage;
        loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
        messagingRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Messaging.fxml")));

        //Sets up login scene
        primaryStage.setTitle("Messaging");
        Scene scene = new Scene(loginRoot, 300, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    //Executes when chat window is closed, clears chat history from server
    public void stop() {
        Queries.deleteMessages();
    }

}

