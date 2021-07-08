package MessagingApplication;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

/*
Custom cell for message cells which differentiate between sender label and sent message on the UI
*/

public class MessageCell extends ListCell<String> {

    //Stores usernames
    private final ArrayList<String> users = new ArrayList<>();

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

    public static Paint randomColorGen() {
        // Member variables (properties about the object)
        String[] mColors = {
                "#39add1", // light blue
                "#3079ab", // dark blue
                "#c25975", // mauve
                "#e15258", // red
                "#f9845b", // orange
                "#838cc7", // lavender
                "#7d669e", // purple
                "#53bbb4", // aqua
                "#51b46d", // green
                "#e0ab18", // mustard
                "#637a91", // dark gray
                "#f092b0", // pink
                "#b7c0c7"  // light gray
        };

        String color;

        // Randomly select a fact
        Random randomGenerator = new Random(); // Construct a new Random number generator
        int randomNumber = randomGenerator.nextInt(mColors.length);
        color = mColors[randomNumber];

        return Color.web(color);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(item);
        setStyle("-fx-background-color: rgb(255,255,255)");

        //If cell is a username cell, style it as such
        if (users.contains(item)) {
            setTextFill(Main.messageColor);
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
