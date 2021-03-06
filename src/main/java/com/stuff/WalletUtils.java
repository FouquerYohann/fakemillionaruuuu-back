package com.stuff;

import static com.stuff.DBUtils.DATE_TIME_FORMATTER;
import static com.stuff.DBUtils.getConnexion;
import static com.stuff.DBUtils.hasEnoughFunds;
import static com.stuff.DBUtils.payFromTo;
import static java.time.LocalDateTime.now;

import java.sql.*;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.stuff.PoloAPI.CURRENCIES;

public class WalletUtils {

    public static JSONObject postOrder(int id, boolean buy, String currencyy, double quantity, double price) {
        CURRENCIES currency = CURRENCIES.valueOf(currencyy);
        Connection connection = DBUtils.getConnexion();
        if (!buy && !DBUtils.hasEnoughFunds(id, currency, quantity)) {
            JSONObject err = new JSONObject().put("err", 412);
            err.put("data", "Not enough " + currency + "to post sale");
        }
        try {
            PreparedStatement insert = connection
                            .prepareStatement("INSERT INTO offres (personid,buy,currency,quantity,price,time_start," +
                                            "offer_uuid) " +
                                            "        VALUES (?, ?, ?, ?, ?, ?, ?);");
            insert.setInt(1, id);
            insert.setBoolean(2, buy);
            insert.setString(3, currency.toString());
            insert.setDouble(4, quantity);
            insert.setDouble(5, price);
            insert.setString(6, DATE_TIME_FORMATTER.format(now()));
            insert.setString(7, String.valueOf(UUID.randomUUID()));

            if (insert.executeUpdate() == 1) {
                insert.close();
                connection.close();
                return new JSONObject().put("err", 200);
            }
            insert.close();
            connection.close();
            return new JSONObject().put("err", 417);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JSONObject().put("err", 417);
    }

    public static JSONObject checkForSales(int id, boolean buy, String currency, double myQuantity, double
                    myPrice) {

        CURRENCIES currencies = CURRENCIES.valueOf(currency);
        boolean funds = (buy) ? hasEnoughFunds(id, CURRENCIES.BTC, myQuantity * myPrice) : hasEnoughFunds(id,
                        currencies, myQuantity * myPrice);
        if (!funds) {
            return new JSONObject().put("err", 603).put("data", "not enough funds");
        }
        return buy ? buyOrder(id, currencies, myQuantity, myPrice) : sellOrder(id, currencies, myQuantity, myPrice);
    }

    private static JSONObject sellOrder(int id, CURRENCIES currencies, double myQuantity, double myPrice) {
        Connection connexion = getConnexion();
        try {
            PreparedStatement query = connexion
                            .prepareStatement("SELECT * FROM offres where currency = ? AND price >= ? AND buy = TRUE" +
                                            " ORDER BY price ASC;");
            query.setString(1, currencies.toString());
            query.setDouble(2, myPrice);

            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                UUID offerUUID = UUID.fromString(resultSet.getString("offer_uuid"));
                int personID = resultSet.getInt("personid");
                double otherPrice = resultSet.getDouble("price");
                double otherQuantity = resultSet.getDouble("quantity");

                if (otherQuantity <= myQuantity) {

                    myQuantity -= otherQuantity;
                    payFromTo(personID, id, currencies.toString(), otherQuantity, otherPrice);

                    moveOrderToLogs(offerUUID, id);

                } else {
                    PreparedStatement updt = connexion
                                    .prepareStatement("UPDATE offres SET quantity = quantity + ? WHERE offer_uuid = ?");
                    updt.setDouble(1, -myQuantity);
                    updt.setString(2, offerUUID.toString());

                    if (updt.executeUpdate() != 1) {
                        return new JSONObject().put("err", 416);
                    }

                    payFromTo(personID, id, currencies.toString(), myQuantity, otherPrice);
                    addOfferToLogs(personID, id, currencies, myQuantity, myPrice, UUID.randomUUID());

                    myQuantity = 0;
                }

                if (myQuantity <= 0) {
                    resultSet.close();
                    query.close();
                    connexion.close();
                    return new JSONObject().put("err", 200);
                }

            }
            query.close();
            connexion.close();
            postOrder(id, false, currencies.toString(), myQuantity, myPrice);
        } catch (SQLException e1) {
            e1.printStackTrace();
            return new JSONObject().put("err", 601);
        }
        return new JSONObject().put("err", 200);
    }

    private static JSONObject buyOrder(int id, CURRENCIES currencies, double myQuantity, double myPrice) {
        Connection connexion = getConnexion();
        try {
            PreparedStatement query = connexion
                            .prepareStatement("SELECT * FROM offres where currency = ? AND price <= ? AND buy = FALSE" +
                                            " ORDER BY price ASC;");
            query.setString(1, currencies.toString());
            query.setDouble(2, myPrice);

            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                UUID offerUUID = UUID.fromString(resultSet.getString("offer_uuid"));
                int personID = resultSet.getInt("personid");
                double otherPrice = resultSet.getDouble("price");
                double otherQuantity = resultSet.getDouble("quantity");

                if (otherQuantity <= myQuantity) {

                    myQuantity -= otherQuantity;
                    payFromTo(id, personID, currencies.toString(), otherQuantity, otherPrice)
                                    .getInt("err");
                    moveOrderToLogs(offerUUID, id);

                } else {
                    PreparedStatement updt = connexion
                                    .prepareStatement("UPDATE offres SET quantity = quantity + ? WHERE offer_uuid = ?");
                    updt.setDouble(1, -myQuantity);
                    updt.setString(2, offerUUID.toString());

                    if (updt.executeUpdate() != 1) {
                        return new JSONObject().put("err", 416);
                    }

                    payFromTo(id, personID, currencies.toString(), myQuantity, otherPrice);
                    addOfferToLogs(id, personID, currencies, myQuantity, myPrice, UUID.randomUUID());

                    myQuantity = 0;
                }

                if (myQuantity <= 0) {
                    resultSet.close();
                    query.close();
                    connexion.close();
                    return new JSONObject().put("err", 200);
                }

            }
            resultSet.close();
            query.close();
            connexion.close();
            postOrder(id, true, currencies.toString(), myQuantity, myPrice);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return new JSONObject().put("err", 200);

    }

    private static void addOfferToLogs(int buyer, int seller, CURRENCIES currency, double quantity, double price,
                    UUID uuid) {
        Connection connection = DBUtils.getConnexion();
        try {

            PreparedStatement insert = connection
                            .prepareStatement("INSERT INTO offre_logs (buyer,seller,currency,quantity,price," +
                                            "time_finished,offre_uuid) " +
                                            "        VALUES (?, ?, ?, ?, ?, ?, ?);");

            insert.setInt(1, buyer);
            insert.setInt(2, seller);
            insert.setString(3, currency.toString());
            insert.setDouble(4, quantity);
            insert.setDouble(5, price);
            insert.setString(6, DATE_TIME_FORMATTER.format(now()));
            insert.setString(7, uuid.toString());

            insert.executeUpdate();
            insert.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void supprOrder(UUID offerUUID) {
        Connection connection = getConnexion();
        try {
            PreparedStatement suppr = connection.prepareStatement("DELETE from offres WHERE offer_uuid = ?");
            suppr.setString(1, offerUUID.toString());

            suppr.executeUpdate();

            suppr.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void moveOrderToLogs(UUID offerUUID, int id) {
        Connection connexion = getConnexion();
        PreparedStatement updt = null;
        try {
            PreparedStatement query = connexion.prepareStatement("SELECT * FROM offres WHERE offer_uuid = ?");
            query.setString(1, offerUUID.toString());

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                int personid = resultSet.getInt("personid");
                String currency = resultSet.getString("currency");
                double quantity = resultSet.getDouble("quantity");
                boolean buying = resultSet.getBoolean("buy");
                double price = resultSet.getDouble("price");

                supprOrder(offerUUID);
                if (buying) {
                    addOfferToLogs(personid, id, CURRENCIES.valueOf(currency), quantity, price, offerUUID);
                } else {
                    addOfferToLogs(id, personid, CURRENCIES.valueOf(currency), quantity, price, offerUUID);
                }
            }
            resultSet.close();
            query.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static JSONArray getTrades(String currency) {
        Connection connexion = getConnexion();
        try {
            System.out.println(currency);
            PreparedStatement query = connexion.prepareStatement("SELECT * FROM offres WHERE currency=?");
            query.setString(1, currency);

            ResultSet resultSet = query.executeQuery();

            JSONArray retour = new JSONArray();
            while (resultSet.next()) {
                JSONObject tmp = new JSONObject();
                tmp.put("personid", resultSet.getInt("personid"));
                tmp.put("currency", resultSet.getString("currency"));
                tmp.put("quantity", resultSet.getDouble("quantity"));
                tmp.put("buying", resultSet.getBoolean("buy"));
                tmp.put("price", resultSet.getDouble("price"));
                tmp.put("offer_uuid", resultSet.getString("offer_uuid"));
                retour.put(tmp);
            }
            resultSet.close();
            query.close();
            connexion.close();
            return retour;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getTradesHistory(String currency) {
        Connection connexion = getConnexion();
        try {
            System.out.println(currency);
            PreparedStatement query = connexion.prepareStatement("SELECT * FROM offre_logs WHERE currency=?");
            query.setString(1, currency);

            ResultSet resultSet = query.executeQuery();

            JSONArray retour = new JSONArray();
            while (resultSet.next()) {
                JSONObject tmp = new JSONObject();
                tmp.put("currency", resultSet.getString("currency"));
                tmp.put("quantity", resultSet.getDouble("quantity"));
                tmp.put("time_finished", resultSet.getString("time_finished"));
                tmp.put("price", resultSet.getDouble("price"));
                retour.put(tmp);
            }
            resultSet.close();
            query.close();
            connexion.close();
            return retour;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
