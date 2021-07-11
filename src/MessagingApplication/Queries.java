package MessagingApplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Queries {

    Queries() {
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
}
