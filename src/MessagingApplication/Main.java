package MessagingApplication;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {

    //Creates JDBC SQL Server connection object
    public static Connection conn = Queries.getConnection();

    //Local copy of messages
    public static ObservableList<String> localMessages = FXCollections.observableArrayList();

    //Stores client information
    public static String sessionUserAccountID;
    public static String sessionUsername;
    public static String sessionUser;
    public static String sessionUserMessageColor;
    public static Boolean flag = false;

    //Launches JavaFX
    public static void main(String[] args) {
        launch(args);
    }

    //Entry point to application, handles login screen logic
    @Override
    public void start(Stage primaryStage) throws Exception {

        //Sets up login scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
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
        loginBtn.setOnMouseClicked((event) -> {
            //Gets account login information from server and compares to entered values
            ArrayList<String> usernames = Queries.getUsernames();
            ArrayList<String> passwords = Queries.getPasswords();
            String enteredUsername = userField.getText();
            String enteredPassword = passwordField.getText();

            if (usernames.contains(enteredUsername) && passwords.get(usernames.indexOf(enteredUsername)).equals(enteredPassword)) {
                System.out.println("You're in!");
                sessionUsername = userField.getText();
                sessionUserAccountID = Queries.getAccountIDFromUsername(sessionUsername);
                sessionUser = Queries.getName(sessionUserAccountID);
                sessionUserMessageColor = Queries.getPrefColor(sessionUserAccountID);
                wrongPasswordLbl.setVisible(false);

                try {
                    messagingScreen(primaryStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                wrongPasswordLbl.setVisible(true);
            }
        });
    }

    //Messaging Screen Logic
    public void messagingScreen(Stage primaryStage) throws IOException {

        //Sets up messaging scene
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Messaging.fxml")));
        primaryStage.setTitle("Messaging");
        Scene scene = new Scene(root, 300, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        //Instantiates messaging scene components
        Button refreshBtn = (Button) scene.lookup("#refreshBtn");
        Button sendBtn = (Button) scene.lookup("#sendBtn");
        TextArea composeArea = (TextArea) scene.lookup("#composeArea");
        ListView<String> messageDisplay = (ListView<String>) scene.lookup("#messageDisplay");
        Text textHolder = new Text();

        //Enables custom cell factory which differentiates between sender label and sent message on the UI
        messageDisplay.setCellFactory(list -> new MessageCell());

        //Dynamically changes height of message composition area with size of message
        double lineWidth = 184;
        composeArea.setWrapText(true);
        textHolder.textProperty().bind(composeArea.textProperty());
        textHolder.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            double textWidth = textHolder.getLayoutBounds().getWidth();
            double textHeight = textHolder.getLayoutBounds().getHeight();
            System.out.println(textHeight);
            System.out.println(composeArea.getHeight());
            composeArea.setMinHeight(25 + (15.9609375 * Math.floor(textWidth / lineWidth)));
        });

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

            //Executes
            Queries.sendMessage(message2send);

            //Refreshes display & clears text field
            refreshBtn.fire();
            composeArea.clear();
        });
    }

    //Updates message display ListView with new messages
    public void updateMessageDisplay(ListView<String> messageDisplay) throws SQLException {

        //Gets currently displayed messages
        ObservableList<String> currentMessages = Queries.getMessages();

        //Computes number of new messages
        int oldConversationSize = localMessages.size();
        int currentConversationSize = currentMessages.size();
        int newMessageNumber = currentConversationSize - oldConversationSize;

        //Adds new messages to local copy & updates UI
        for (int x = currentConversationSize - newMessageNumber; x < currentConversationSize; x++) {
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
}

