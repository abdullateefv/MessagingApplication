package MessagingApplication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {

    @FXML
    private Rectangle loginButton;
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label wrongPasswordLbl;

    public LoginController() {

    }

    @FXML
    //Called after corresponding FXML document is loaded
    private void initialize() {

    }

    @FXML
    //Login button action handler
    private void onLoginBtnClicked() {

        //Changes color of button to confirm click
        loginButton.setStyle("-fx-fill: #0052a0");

        //Gets entered authentication information from respective fields
        String enteredUsername = userField.getText();
        String enteredPassword = passwordField.getText();

        try {
            //Attempts login, throws SQLException authFail if wrong username or password
            Main.conn = Queries.getConnection(enteredUsername, enteredPassword);

            //Queries & initializes user information
            Main.sessionUsername = userField.getText();
            Main.sessionUser = Queries.getName(Main.sessionUsername);
            Main.sessionUserMessageColor = Queries.getPrefColor(Main.sessionUsername);

            //Loads messaging view if successful login
            Parent messagingRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Messaging.fxml")));
            Main.primaryStage.setTitle("Messaging");
            Scene scene = new Scene(messagingRoot, 800, 600);
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();

            //Hides wrong password message
            wrongPasswordLbl.setVisible(false);

        } catch (SQLException authFail) {
            //Displays wrong password message
            wrongPasswordLbl.setVisible(true);

        } catch (IOException throwables) {
            throwables.printStackTrace();
        }

    }

    //Change login button color to confirm click
    public void onLoginBtnRelease() {
        loginButton.setStyle("-fx-fill: #5eb1ff");
    }

    //Change login button color to confirm click
    public void onLoginBtnMouseEnter() {
        loginButton.setStyle("-fx-fill: #009cff");
    }

    //Change login button color to confirm click
    public void onLoginBtnMouseExit() {
        loginButton.setStyle("-fx-fill: #5eb1ff");
    }
}
