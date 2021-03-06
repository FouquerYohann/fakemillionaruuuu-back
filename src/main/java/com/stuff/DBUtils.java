package com.stuff;

import static com.stuff.PoloAPI.CURRENCIES.BTC;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.util.UUID.randomUUID;
import static javax.servlet.http.HttpServletResponse.SC_EXPECTATION_FAILED;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.json.JSONObject;

import com.stuff.PoloAPI.CURRENCIES;

public class DBUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static String url = "jdbc:postgresql://ec2-54-228-235-198.eu-west-1.compute.amazonaws" +
                    ".com:5432/d6ton9gfh7lpe0?user=gixohaloohklfj&password" +
                    "=3df085090c4a659de03ea879e983cb727006a5d444da76738eb4abda5893cbec&sslmode=require";
    private static Connection connexion = null;

    private static final JSONObject REPONSE_OK = new JSONObject().put("err", SC_OK);

    public static Connection getConnexion() {
        try {

            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            if (dbUrl == null || dbUrl.isEmpty())
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
            PreparedStatement preparedStatement = connexion.prepareStatement("SELECT * FROM users WHERE login=?;");
            preparedStatement.setString(1, login);

            ResultSet result = preparedStatement.executeQuery();

            int id = -1;
            if (result.next()) {
                String log = result.getString("login");
                String pass = result.getString("password");
                id = result.getInt("PersonID");
                if (log.equals(login) && pass.equals(password)) {
                    String uuid = addSession(id);
                    reponse.put("err", SC_OK);
                    reponse.put("login", login);
                    reponse.put("session", uuid);
                    reponse.put("personId", id);

                    result.close();
                    preparedStatement.close();
                    connexion.close();
                    return reponse;
                }
            }
            result.close();
            preparedStatement.close();
            connexion.close();

        } catch (Exception e) {
            e.printStackTrace();
            reponse.put("err", SC_EXPECTATION_FAILED);
            return reponse;
        }
        reponse.put("err", SC_EXPECTATION_FAILED);
        return reponse;
    }

    public static String addSession(int id) {
        String previousSession = checkSession(id);
        if (previousSession != null) {
            return previousSession;
        }
        connexion = getConnexion();
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
                preparedStatement.close();
                connexion.close();
                return uuid;
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public static JSONObject closeSession(String login) {
        int id = getIdFromLogin(login);
        return closeSession(id);
    }

    public static JSONObject closeSession(int id) {
        connexion = getConnexion();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connexion.prepareStatement("UPDATE sessions\n" +
                            "SET valid = ? WHERE personid = ?;");

            preparedStatement.setBoolean(1, false);
            preparedStatement.setInt(2, id);

            int i = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();
            return (i > 0) ? REPONSE_OK : new JSONObject().put("err", SC_EXPECTATION_FAILED);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JSONObject().put("err", SC_EXPECTATION_FAILED);
    }

    public static String checkSession(int id) {
        connexion = getConnexion();
        try {

            PreparedStatement preparedStatement = connexion
                            .prepareStatement("SELECT * FROM sessions WHERE personid = ? AND valid = TRUE ;");
            PreparedStatement UpdateStatement = connexion.prepareStatement("UPDATE sessions\n" +
                            "SET last_time = ? WHERE PersonID = ? AND valid = TRUE;");

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                LocalDateTime last = parse(resultSet.getString("last_time"), DATE_TIME_FORMATTER);
                if (last.until(now(), ChronoUnit.MINUTES) > 5) {
                    JSONObject closed = closeSession(id);
                    if (closed.getString("err").equals(SC_OK)) {
                        resultSet.close();
                        preparedStatement.close();
                        connexion.close();
                        return null;
                    }
                } else {
                    UpdateStatement.setString(1, DATE_TIME_FORMATTER.format(now()));
                    UpdateStatement.setInt(2,id);
                    String session_uuid = resultSet.getString("session_uuid");
                    UpdateStatement.executeUpdate();
                    resultSet.close();
                    UpdateStatement.close();
                    connexion.close();
                    return session_uuid;
                }
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean alreadyExist(String login) {
        connexion = getConnexion();
        try {
            PreparedStatement preparedStatement = connexion.prepareStatement("SELECT * FROM users WHERE login = ? ;");

            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                resultSet.close();
                preparedStatement.close();
                return true;
            }
            preparedStatement.close();
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static JSONObject inscription(String login, String password,String mail) {

        connexion = getConnexion();
        JSONObject reponse = new JSONObject();
        try {
            if (alreadyExist(login)) {
                reponse.put("err", SC_EXPECTATION_FAILED);
                connexion.close();
                return reponse;
            }

            PreparedStatement preparedStatement = connexion
                            .prepareStatement("INSERT INTO users (Login, Password, mail) " +
                                            "VALUES (?, ?, ?);");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, mail);

            int i = preparedStatement.executeUpdate();
            if (i == 1) {
                createWallet(getIdFromLogin(login), 10, 10, 10, 10, 10, 10);
                preparedStatement.close();
                connexion.close();
                reponse.put("err", SC_OK);
                return reponse;
            } else {
                preparedStatement.close();
                throw new SQLException("value not inserted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new JSONObject().put("err", 601);
        }
    }

    public static boolean hasEnoughFunds(int id, CURRENCIES currency, double value) {
        connexion = getConnexion();
        try {
            PreparedStatement preparedStatement = connexion
                            .prepareStatement("SELECT * FROM wallet WHERE personid = ? ;");
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getDouble(currency.toString()) < value) {
                    resultSet.close();
                    preparedStatement.close();
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static JSONObject changeValue(int id, String currency, double delta) {
        connexion = getConnexion();
        try {
            PreparedStatement preparedStatement = connexion
                            .prepareStatement("UPDATE wallet SET " + currency + " = " + currency + " + ? WHERE " +
                                            "personid = ?;");
            preparedStatement.setDouble(1, delta);
            preparedStatement.setInt(2, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();
            return REPONSE_OK;

        } catch (SQLException e) {
            return new JSONObject().put("err", 602).put("data", "not enough " + currency);
        }
    }

    public static JSONObject payFromTo(int buyer, int seller, String currency, double quantity, double
                    price) {

        if (hasEnoughFunds(seller, CURRENCIES.valueOf(currency), quantity) && hasEnoughFunds(buyer, BTC,price*quantity)) {
            changeValue(buyer, BTC.toString(), -price * quantity);
            changeValue(buyer, currency, quantity);
            changeValue(seller, BTC.toString(), price * quantity);
            changeValue(seller, currency, -quantity);
            return REPONSE_OK;
        }
        return new JSONObject().put("err", 602).put("data", "not enough " + currency);
    }

    public static JSONObject createEmptyWallet(int id) {
        return createWallet(id, 0, 0, 0, 0, 0, 0);
    }

    public static JSONObject createWallet(int id, float btc, float eth, float ltc, float xrp, float bch, float dash) {

        connexion = getConnexion();
        JSONObject reponse = new JSONObject();
        try {
            PreparedStatement preparedStatement = connexion
                            .prepareStatement("INSERT INTO wallet (personid, btc, eth, xrp, ltc, dash, bch) VALUES " +
                                            "(?, ?, ?, ?, ?, ?, ?);");
            preparedStatement.setInt(1, id);
            preparedStatement.setFloat(2, btc);
            preparedStatement.setFloat(3, eth);
            preparedStatement.setFloat(4, xrp);
            preparedStatement.setFloat(5, ltc);
            preparedStatement.setFloat(6, dash);
            preparedStatement.setFloat(7, bch);
            int i = preparedStatement.executeUpdate();
            preparedStatement.close();
            connexion.close();
            if (i == 1) {
                reponse.put("err", SC_OK);
                return reponse;
            } else {
                throw new SQLException("value not inserted");
            }
        } catch (SQLException e) {
            return new JSONObject().put("err", 601);
        }
    }

    public static JSONObject getWalletValue(int id) {
        connexion = getConnexion();
        try {
            PreparedStatement preparedStatement = connexion
                            .prepareStatement("SELECT * FROM wallet WHERE personid = ? ;");

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                JSONObject toReturn = new JSONObject();
                toReturn.put("BTC", resultSet.getFloat("BTC"));
                toReturn.put("LTC", resultSet.getFloat("LTC"));
                toReturn.put("ETH", resultSet.getFloat("ETH"));
                toReturn.put("XRP", resultSet.getFloat("XRP"));
                toReturn.put("BCH", resultSet.getFloat("BCH"));
                toReturn.put("DASH", resultSet.getFloat("DASH"));
                resultSet.close();
                preparedStatement.close();
                return toReturn;
            }
            preparedStatement.close();
            connexion.close();
            return new JSONObject().put("err", 601);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getLoginFromId(int id) {
        connexion = getConnexion();
        try {
            PreparedStatement preparedStatement = connexion
                            .prepareStatement("SELECT * FROM users WHERE personid = ? ;");

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String login = resultSet.getString("login");
                resultSet.close();
                preparedStatement.close();
                return login;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getIdFromLogin(String login) {
        connexion = getConnexion();
        try {
            PreparedStatement preparedStatement = connexion.prepareStatement("SELECT * FROM users WHERE login = ? ;");

            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int personid = resultSet.getInt("personid");
                resultSet.close();
                preparedStatement.close();
                return personid;
            }
            resultSet.close();
            preparedStatement.close();
            connexion.close();
            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
