package MessagingApplication;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.SQLException;

public class MessagingController {

    @FXML
    private ListView<String> messageDisplay;
    @FXML
    private TextArea composeArea;

    private final Text textHolder = new Text();
    private final double lineWidth = 184;

    public MessagingController() {
    }

    @FXML
    private void initialize() {
        //Starts conversation auto-refresh service
        Timeline messageRefresh = new Timeline(
                new KeyFrame(Duration.seconds(1),
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
            double textHeight = textHolder.getLayoutBounds().getHeight();
            System.out.println(textHeight);
            System.out.println(composeArea.getHeight());
            composeArea.setMinHeight(25 + (15.9609375 * Math.floor(textWidth / lineWidth)));
        });
    }

    @FXML
    private void onSendBtnAction() {
        //Gets new message from text field
        String message2send = composeArea.getText();

        //Executes
        Queries.sendMessage(message2send);

        //Refreshes display & clears text field
        try {
            updateMessageDisplay(messageDisplay);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        composeArea.clear();
    }

    //Updates message display ListView with new messages
    private static void updateMessageDisplay(ListView<String> messageDisplay) throws SQLException {

        //Gets messages in server
        ObservableList<String> currentMessages = Queries.getMessages();

        //Computes number of new messages in server
        int oldConversationSize = Main.localMessages.size();
        int currentConversationSize = currentMessages.size();
        int newMessageNumber = currentConversationSize - oldConversationSize;

        //Adds new messages to local copy & updates UI
        for (int x = currentConversationSize - newMessageNumber; x < currentConversationSize; x++) {
            Main.localMessages.add(currentMessages.get(x));
            messageDisplay.setItems(Main.localMessages);
        }
    }

}
