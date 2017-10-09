package com.stuff;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.util.UUID.randomUUID;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;

public class DBUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static String url = "jdbc:postgresql://ec2-54-228-235-198.eu-west-1.compute.amazonaws.com:5432/d6ton9gfh7lpe0?user=gixohaloohklfj&password=3df085090c4a659de03ea879e983cb727006a5d444da76738eb4abda5893cbec&sslmode=require";
    private static String utilisateur = "java";
    private static String mdp = "theSuperPassword";
    private static Connection connexion = null;

    private static final JSONObject REPONSE_OK = new JSONObject().put("resp", "ok");

    private static Connection getConnexion() {
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

    public static JSONObject login(String login, String password) {
        JSONObject reponse = new JSONObject();
        try {
            connexion = getConnexion();
            PreparedStatement query = connexion.prepareStatement("SELECT * FROM users WHERE login=?;");
            query.setString(1, login);

            ResultSet result = query.executeQuery();

            int id = -1;
            while (result.next()) {
                String log = result.getString("login");
                String pass = result.getString("password");
                id = result.getInt("PersonID");
                if (log.equals(login) && pass.equals(password)) {
                    String uuid = addSession(id);
                    reponse.put("resp","ok");
                    reponse.put("login", login);
                    reponse.put("session", uuid);
                    return reponse;
                }
            }


        } catch (Exception e) {
            reponse.put("resp", "ko");
            reponse.put("err", 124);
            return reponse;
        } finally {
            try {

                if (connexion != null) {connexion.close();}
            } catch (SQLException e) {
                e.printStackTrace();
                reponse.put("resp", "ko");
                reponse.put("err", 125);
                return reponse;
            }
        }
        reponse.put("resp", "ko");
        reponse.put("err", 123);
        return reponse;
    }

    public static String addSession(int id) {
        if (checkSession(id)) {
            return null;
        }
        if (connexion == null) {
            connexion = getConnexion();
        }
        try {
            String uuid = randomUUID().toString();
            PreparedStatement preparedStatement = connexion
                            .prepareStatement("INSERT INTO sessions (PersonID, Session_uuid, last_time, valid ) " +
                                            "        VALUES (?, ?, ?, ?);");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, uuid);
            preparedStatement.setString(3, DATE_TIME_FORMATTER.format(now()));
            preparedStatement.setBoolean(4, true);
            int i = preparedStatement.executeUpdate();
            if (i == 1) {
                return uuid;
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public static JSONObject closeSession(int id) {
        if (connexion == null) {
            connexion = getConnexion();
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connexion.prepareStatement("UPDATE sessions\n" +
                            "SET valid = ? WHERE personid = ?;");

            preparedStatement.setBoolean(1, false);
            preparedStatement.setInt(2, id);

            int i = preparedStatement.executeUpdate();
            if (i == 1) {
                return REPONSE_OK;
            }
            return new JSONObject().put("resp", "ko");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  new JSONObject().put("resp", "ko");
    }




    public static boolean checkSession(int id) {
        if (connexion == null) {
            connexion = getConnexion();
        }
        try {

            PreparedStatement query = connexion
                            .prepareStatement("SELECT * FROM sessions WHERE personid = ? AND valid = TRUE ;");
            PreparedStatement update = connexion.prepareStatement("UPDATE sessions\n" +
                            "SET last_time = ? WHERE PersonID = id AND valid = TRUE;");

            query.setInt(1, id);
            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                LocalDateTime last = parse(resultSet.getString("last_time"),DATE_TIME_FORMATTER);
                if (last.until(now(), ChronoUnit.MINUTES) > 5) {
                    JSONObject closed = closeSession(id);
                    if (closed.getString("resp").equals("ok")) {
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
            PreparedStatement preparedStatement = connexion.prepareStatement("SELECT * FROM users WHERE Login = ? ;");

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

    public static JSONObject inscription(String login, String password) {

        connexion = getConnexion();
        JSONObject reponse = new JSONObject();
        try {
            if (alreadyExist(login)) {
                reponse.put("resp", "ko");
                reponse.put("err", 123);
                return reponse;
            }
            PreparedStatement preparedStatement = connexion
                            .prepareStatement("INSERT INTO users (Login, Password) " +
                                            "VALUES (?, ?);");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            int i = preparedStatement.executeUpdate();
            if (i == 1) {
                reponse.put("resp", "ok");
                return reponse;
            }
            reponse.put("resp", "ko");
            return reponse;
        } catch (SQLException e) {
            return new JSONObject().put("resp", "SQLException");
        }
    }
}
