package MessagingApplication;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import java.io.IOException;
import java.sql.*;

public class Main extends Application {

    //Local copy of messages
    public static ObservableList<String> localMessages = FXCollections.observableArrayList();

    //Creates JDBC SQL Server connection object
    public static Connection conn;
    static {
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://abdullateefv.database.windows.net:1433;database=ChatApp;user=abdullateef@abdullateefv;password=Password123;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Entry point to application, handles login screen logic
    @Override
    public void start(Stage primaryStage) throws Exception {

        //Sets up login scene
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        primaryStage.setTitle("Messaging");
        Scene scene = new Scene(root, 300, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        //Instantiates login scene components
        Rectangle loginBtn = (Rectangle) scene.lookup("#loginButton");
        TextField userField = (TextField) scene.lookup("#userField");
        PasswordField passwordField = (PasswordField) scene.lookup("#passwordField");
        Label wrongPasswordLbl = (Label) scene.lookup("#wrongPasswordLbl");

        //Handles log in button action
        loginBtn.setOnMouseClicked((event)->{
            boolean login = false;

            //Gets account login information from server and compares to entered values
            try {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("SELECT Usernames, Passwords FROM ChatApp.dbo.logins");
                while (rs.next()) {
                    String aUsername = rs.getString("Usernames");
                    String aPassword = rs.getString("Passwords");

                    if (userField.getText().equals(aUsername) && passwordField.getText().equals(aPassword)) {
                        login = true;
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //Successful login
            if (login) {
                System.out.println("You're in!");
                wrongPasswordLbl.setVisible(false);
                final String sessionUser = userField.getText();
                try {
                    messagingScreen(primaryStage, sessionUser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            //Unsuccessful Login
            } else {
                wrongPasswordLbl.setVisible(true);
            }
        });
    }

    //Messaging Screen Logic
    private void messagingScreen(Stage primaryStage, String sessionUser) throws IOException {

        //Sets up messaging scene
        Parent root = FXMLLoader.load(getClass().getResource("Messaging.fxml"));
        primaryStage.setTitle("Messaging");
        Scene scene = new Scene(root, 300, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        //Instantiates messaging scene components
        Button refreshBtn = (Button) scene.lookup("#refreshBtn");
        Button sendBtn = (Button) scene.lookup("#sendBtn");
        TextField composeArea = (TextField) scene.lookup("#composeArea");
        ListView<String> messageDisplay = (ListView) scene.lookup("#messageDisplay");

        //Enables custom cell factory which differentiates between sender label and sent message on the UI
        messageDisplay.setCellFactory(list -> new MessageCell());

        //Starts conversation auto-refresh service
        Timeline messageRefresh = new Timeline(
            new KeyFrame(Duration.seconds(1),
                    event -> refreshBtn.fire()));

        messageRefresh.setCycleCount(Timeline.INDEFINITE);
        messageRefresh.play();

        //Handles refresh chat button action
        refreshBtn.setOnAction((event) -> {
            try {
                updateMessageDisplay(messageDisplay);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });



        //Handles send button action
        sendBtn.setOnAction((event) -> {

            //Gets new message from text field
            String message2send = composeArea.getText();

            //Updates SQL server with new message
            try {
                Statement statement = conn.createStatement();
                statement.executeUpdate("INSERT INTO ChatApp.dbo.messages VALUES ('"+message2send+"','"+sessionUser+"')");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            //Refreshes & clears text field
            refreshBtn.fire();
            composeArea.clear();
        });
    }

    //Updates message display ListView with new messages
    private void updateMessageDisplay(ListView<String> messageDisplay) throws SQLException {

        //Selects all messages currently in JDBC SQL server & stores in temporary "currentMessages" array
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT message, sender FROM ChatApp.dbo.messages");
        ObservableList<String> currentMessages = FXCollections.observableArrayList();

        while (rs.next()) {
            currentMessages.add(rs.getString("sender"));
            currentMessages.add(rs.getString("message"));
        }

        //Computes number of new messages
        int oldConversationSize = localMessages.size();
        int currentConversationSize = currentMessages.size();
        int newMessageNumber = currentConversationSize - oldConversationSize;

        //Adds new messages to local copy & updates UI
        for (int x = currentMessages.size() - newMessageNumber; x < currentConversationSize; x++) {
            localMessages.add(currentMessages.get(x));
            messageDisplay.setItems(localMessages);
        }
    }

    //Executes when chat window is closed, clears chat history from SQL Server
    @Override
    public void stop() throws Exception {
        Statement statement = conn.createStatement();
        statement.executeUpdate("TRUNCATE TABLE ChatApp.dbo.messages");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

