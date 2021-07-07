package MessagingApplication;

import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/*
Custom cell for message cells which differentiate between sender label and sent message on the UI
*/

public class MessageCell extends ListCell<String> {

    //Stores usernames
    private ArrayList<String> users = new ArrayList<>();

    public MessageCell() {
        //Gets usernames from database
        try {
            Statement statement = Main.conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Usernames FROM ChatApp.dbo.logins");
            while (rs.next()) {
                users.add(rs.getString("Usernames"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void updateItem(String item, boolean empty) {

        super.updateItem(item, empty);
        setText(item);
        setStyle("-fx-background-color: rgb(255,255,255)");

        //If cell is a username cell, style it as such
        if (users.contains(item)) {
            setTextFill(Color.DARKOLIVEGREEN);
            setFont(Font.font("System", FontWeight.BOLD, 12));
        } else {
            setTextFill(Color.BLACK);
        }
    }
}
