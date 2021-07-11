package MessagingApplication;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/*
Custom cell for message cells which differentiate between sender label and sent message on the UI
*/

public class MessageCell extends ListCell<String> {

    public MessageCell() {
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(item);
        setStyle("-fx-background-color: rgb(255,255,255)");

        //If cell is a username cell, style it as such
        if (Queries.getNames().contains(item)) {
            setTextFill(Color.web(Main.sessionUserMessageColor));
            setFont(Font.font("System", FontWeight.BOLD, 12));
            Main.flag = Main.sessionUser.equals(item);
        } else {
            setTextFill(Color.BLACK);
        }

        if (Main.flag)
            setAlignment(Pos.CENTER_RIGHT);
        else
            setAlignment(Pos.CENTER_LEFT);

    }
}
