package MessagingApplication;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class Queries {

    Queries() {
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://abdullateefv.database.windows.net:1433;database=ChatApp;user=abdullateef@abdullateefv;password=Password123;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public static String getAccountIDFromUsername(String username) {
        String accountID = null;
        try {
            Statement statement = Main.conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Account_ID FROM dbo.Accounts WHERE (Username = '" + username + "')");
            while (rs.next()) {
                accountID = rs.getString("Account_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return accountID;
    }

    public static String getPrefColor(String accountID) {
        String prefColorHex = null;
        try {
            Statement statement = Main.conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT PrefMessageColor FROM dbo.Accounts WHERE (Account_ID = '" + accountID + "')");
            while (rs.next()) {
                prefColorHex = rs.getString("PrefMessageColor");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return prefColorHex;
    }

    public static String getName(String accountID) {
        String name = null;
        try {
            Statement statement = Main.conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Name FROM dbo.Accounts WHERE (Account_ID = '" + accountID + "')");
            while (rs.next()) {
                name = rs.getString("Name");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return name;
    }

    public static ArrayList<String> getUsernames() {
        ArrayList<String> usernames = new ArrayList<>();
        try {
            Statement statement = Main.conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Username FROM dbo.Accounts");
            while (rs.next()) {
                usernames.add(rs.getString("Username"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return usernames;
    }

    public static ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        try {
            Statement statement = Main.conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Name FROM dbo.Accounts");
            while (rs.next()) {
                names.add(rs.getString("Name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return names;
    }

    public static ArrayList<String> getPasswords() {
        ArrayList<String> passwords = new ArrayList<>();
        try {
            Statement statement = Main.conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Password FROM dbo.Accounts");
            while (rs.next()) {
                passwords.add(rs.getString("Password"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return passwords;
    }

    public static void sendMessage(String message2send) {
        try {
            Statement statement = Main.conn.createStatement();
            statement.executeUpdate("INSERT INTO ChatApp.dbo.messages VALUES ('" + message2send + "','" + Main.sessionUser + "')");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static ObservableList<String> getMessages() {
        ObservableList<String> messages = FXCollections.observableArrayList();
        try {
            Statement statement = Main.conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT message, sender FROM ChatApp.dbo.messages");
            while (rs.next()) {
                messages.add(rs.getString("sender"));
                messages.add(rs.getString("message"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return messages;
    }
}
