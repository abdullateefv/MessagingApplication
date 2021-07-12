package MessagingApplication;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class LoginController {
    public TextField userField;
    public Rectangle loginButton;
    public PasswordField passwordField;
    public Label wrongPasswordLbl;

    public LoginController() {

    }

    public void initialize() {

    }

    public void onLoginClicked() {
        //Gets account login information from server and compares to entered values
        ArrayList<String> usernames = Queries.getUsernames();
        ArrayList<String> passwords = Queries.getPasswords();
        String enteredUsername = userField.getText();
        String enteredPassword = passwordField.getText();

        if (usernames.contains(enteredUsername) && passwords.get(usernames.indexOf(enteredUsername)).equals(enteredPassword)) {
            System.out.println("You're in!");
            Main.sessionUsername = userField.getText();
            Main.sessionUserAccountID = Queries.getAccountIDFromUsername(Main.sessionUsername);
            Main.sessionUser = Queries.getName(Main.sessionUserAccountID);
            Main.sessionUserMessageColor = Queries.getPrefColor(Main.sessionUserAccountID);
            wrongPasswordLbl.setVisible(false);

            //Sets up messaging scene
            Main.primaryStage.setTitle("Messaging");
            Scene scene = new Scene(Main.root2, 300, 600);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();

        } else {
            wrongPasswordLbl.setVisible(true);
        }
    }
}
