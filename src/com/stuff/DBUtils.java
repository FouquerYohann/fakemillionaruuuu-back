package com.stuff;

import static java.time.LocalDateTime.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class DBUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static String url = "jdbc:postgresql://ec2-54-228-235-198.eu-west-1.compute.amazonaws.com:5432/d6ton9gfh7lpe0?user=gixohaloohklfj&password=3df085090c4a659de03ea879e983cb727006a5d444da76738eb4abda5893cbec&sslmode=require";
    private static String utilisateur = "java";
    private static String mdp = "theSuperPassword";
    private static Connection connexion = null;

    public static Connection getConnexion() {
        try {
            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            if(dbUrl == null || dbUrl.isEmpty())
                dbUrl = url;
            return DriverManager.getConnection(dbUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkLogin(String login, String password) {
        try {
            connexion = getConnexion();

            Statement query = connexion.createStatement();

            ResultSet result = query.executeQuery("SELECT * FROM Users WHERE login='" + login + "';");
            int id = -1;
            while (result.next()) {
                String log = result.getString("login");
                String pass = result.getString("password");
                id = result.getInt("PersonID");
                if (log.equals(login) && pass.equals(password))
                    return true;
            }

            addSession(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connexion != null)
                    connexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean addSession(int id) {
        if (checkSession(id) || alreadyExist(id)) {
            return false;
        }
        if (connexion == null) {
            connexion = getConnexion();
        }
        try {
            PreparedStatement preparedStatement = connexion
                            .prepareStatement("INSERT INTO Sessions (PersonID, Session_uuid, last_time, valid ) " +
                                            "        VALUES (?, ?, ?, ?);");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, UUID.randomUUID().toString());
            preparedStatement.setString(3, DATE_TIME_FORMATTER.format(now()));
            preparedStatement.setBoolean(4, true);
            int i = preparedStatement.executeUpdate();
            if (i == 1) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean closeSession(int id) {
        if (connexion == null) {
            connexion = getConnexion();
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connexion.prepareStatement("UPDATE Sessions\n" +
                            "SET valid = ? WHERE PersonID = ?;");

            preparedStatement.setBoolean(1, false);
            preparedStatement.setInt(2, id);

            int i = preparedStatement.executeUpdate();
            if (i == 1) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkSession(int id) {
        if (connexion == null) {
            connexion = getConnexion();
        }
        try {

            PreparedStatement query = connexion
                            .prepareStatement("SELECT * FROM Sessions WHERE PersonID = ? AND valid = TRUE ;");
            PreparedStatement update = connexion.prepareStatement("UPDATE Sessions\n" +
                            "SET last_time = ? WHERE PersonID = id AND valid = TRUE;");

            query.setInt(1, id);
            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                LocalDateTime last = parse(resultSet.getString("last_time"));
                if (last.until(now(), ChronoUnit.MINUTES) > 5) {
                    boolean closed = closeSession(id);
                    if (closed) {
                        return false;
                    }
                }
                update.setString(1, DATE_TIME_FORMATTER.format(now()));
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean alreadyExist(String login) {
        if (connexion == null) {
            connexion = getConnexion();
        }
        try {
            PreparedStatement preparedStatement = connexion.prepareStatement("SELECT * FROM Users WHERE Login = ? ;");

            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("found one person");
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static boolean alreadyExist(int id) {
        if (connexion == null) {
            connexion = getConnexion();
        }
        try {
            PreparedStatement preparedStatement = connexion
                            .prepareStatement("SELECT * FROM Users WHERE PersonID = ? ;");

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("found one person");
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean addLogin(String login, String password) {

        connexion = getConnexion();
        try {
            if (alreadyExist(login)) {
                System.out.println("Users already exist");
                return false;
            }

            PreparedStatement preparedStatement = connexion
                            .prepareStatement("INSERT INTO Users (Login, Password) " +
                                            "VALUES (?, ?);");

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            int i = preparedStatement.executeUpdate();
            if (i == 1) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
