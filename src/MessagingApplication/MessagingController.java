package MessagingApplication;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.SQLException;

public class MessagingController {

    @FXML
    private ListView conversationsDisplay;
    @FXML
    private Label sessionUserLabel;
    @FXML
    private ListView<String> messageDisplay;
    @FXML
    private TextArea composeArea;

    private final Text textHolder = new Text();
    private final double lineWidth = 184;
    private Timeline messageRefresh;

    public MessagingController() {
    }

    //RECEIVING, Updates message display ListView with new messages from server, is called every second from init
    private static void updateMessageDisplay(ListView<String> messageDisplay) throws SQLException {

        //Gets messages in remote server
        ObservableList<String> remoteMessages = Queries.getMessages();

        //Computes number of new messages in server vs local
        int localConversationSize = Main.localMessages.size();
        int remoteConversationSize = remoteMessages.size();
        int newMessageNumber = remoteConversationSize - localConversationSize;

        //Adds new messages to local copy & updates UI with new messages
        for (int x = remoteConversationSize - newMessageNumber; x < remoteConversationSize; x++) {
            System.out.println(remoteMessages.get(x));
            Main.localMessages.add(remoteMessages.get(x));
            messageDisplay.setItems(Main.localMessages);
        }

    }

    @FXML
    //Called after corresponding FXML document is loaded
    private void initialize() {
        sessionUserLabel.setText(Main.sessionUser);

        //Starts conversation auto-refresh service which repeats every .5 seconds
        messageRefresh = new Timeline(
                new KeyFrame(Duration.seconds(.5),
                        event -> {
                            try {
                                updateMessageDisplay(messageDisplay);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }));
        messageRefresh.setCycleCount(Timeline.INDEFINITE);
        messageRefresh.play();

        //Sets cell factory for message display listView
        messageDisplay.setCellFactory(list -> new MessageCell());

        //Dynamically resizes text composition area as text increases
        textHolder.textProperty().bind(composeArea.textProperty());
        textHolder.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            double textWidth = textHolder.getLayoutBounds().getWidth();
            composeArea.setMinHeight(25 + (15.9609375 * Math.floor(textWidth / lineWidth)));
        });
    }

    @FXML
    //SENDING
    private void onSendBtnAction() {
        //Gets new message from text field
        String message2send = composeArea.getText();

        //Sends message to server
        Queries.sendMessage(message2send);

        //Refreshes display & clears text field
        try {
            updateMessageDisplay(messageDisplay);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        composeArea.clear();
    }

    /* Bugged must fix
    @FXML
    private void onLogoutBtnAction() {
        try {
            Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));

            //Sets up login scene
            Main.primaryStage.setTitle("Messaging");
            Scene scene = new Scene(loginRoot, 300, 600);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();

            //Queries & initializes user information
            Main.sessionUsername = null;
            Main.sessionUserAccountID = null;
            Main.sessionUser = null;
            Main.sessionUserMessageColor = null;

            Main.conn = null;
            messageRefresh.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClearBtnAction() {
        Queries.deleteMessages();
        Main.localMessages.clear();
        messageDisplay.setItems(null);
    }
    */
}
