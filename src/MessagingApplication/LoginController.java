package MessagingApplication;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class LoginController {

    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label wrongPasswordLbl;

    public LoginController() {

    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void onLoginClicked() {
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