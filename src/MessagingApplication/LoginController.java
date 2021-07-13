package MessagingApplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

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

        //Gets entered authentication information from respective fields
        String enteredUsername = userField.getText();
        String enteredPassword = passwordField.getText();

        try {
            //Attempts login, throws SQLException authFail if wrong username or password
            Main.conn = Queries.getConnection(enteredUsername, enteredPassword);

            //Loads messaging view if successful login
            Parent messagingRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Messaging.fxml")));
            Main.primaryStage.setTitle("Messaging");
            Scene scene = new Scene(messagingRoot, 800, 600);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();

            //Queries & initializes user information
            Main.sessionUsername = userField.getText();
            Main.sessionUserAccountID = Queries.getAccountIDFromUsername(Main.sessionUsername);
            Main.sessionUser = Queries.getName(Main.sessionUserAccountID);
            Main.sessionUserMessageColor = Queries.getPrefColor(Main.sessionUserAccountID);

            //Hides wrong password message
            wrongPasswordLbl.setVisible(false);

        } catch (SQLException authFail) {
            //Displays wrong password message
            wrongPasswordLbl.setVisible(true);

        } catch (IOException throwables) {
            throwables.printStackTrace();
        }

    }

}
