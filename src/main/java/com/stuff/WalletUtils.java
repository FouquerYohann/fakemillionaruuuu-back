package com.stuff;

import static com.stuff.DBUtils.DATE_TIME_FORMATTER;
import static com.stuff.DBUtils.getConnexion;
import static java.lang.Math.min;
import static java.time.LocalDateTime.now;

import java.sql.*;
import java.util.UUID;

import org.json.JSONObject;

import com.stuff.PoloAPI.CURRENCIES;

/**
 * Created by Yohann on 26/10/2017.
 */
public class WalletUtils {

    public static JSONObject postOrder(int id, boolean buy, CURRENCIES currency, double quantity, double price) {
        Connection connection = DBUtils.getConnexion();

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
                return new JSONObject().put("err", 200);
            }
            return new JSONObject().put("err", 417);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JSONObject().put("err", 417);
    }

    public static JSONObject checkForSales(int id, boolean buy, CURRENCIES currencies, double myQuantity, double
                    myPrice) {
        return buy ? buyOrder(id, currencies, myQuantity, myPrice) : sellOrder(id, currencies, myQuantity, myPrice);
    }

    private static JSONObject sellOrder(int id, CURRENCIES currencies, double myQuantity, double myPrice) {
        return null;
    }

    private static JSONObject buyOrder(int id, CURRENCIES currencies, double myQuantity, double myPrice) {
        Connection connexion = getConnexion();
        try {
            PreparedStatement query = connexion
                            .prepareStatement("SELECT * FROM offres where currency = ? AND price <= ? AND buy = FALSE" +
                                            " ORDER BY price ASC;");
            query.setString(1, currencies.toString());
            query.setDouble(2, myPrice);

            double myTotalPrice = myQuantity * myPrice;
            double pricePaid = 0;
            ResultSet resultSet = query.executeQuery();
            while (resultSet.next()) {
                UUID offerUUID = UUID.fromString(resultSet.getString("offer_uuid"));
                double otherPrice = resultSet.getDouble("price");
                double otherQuantity = resultSet.getDouble("quantity");

                if (otherQuantity < myQuantity) {


                    myQuantity -= otherQuantity;
                    pricePaid += otherPrice * otherQuantity;
                    //TODO
                    moveOrderToLogs(offerUUID);
                }else{
                    PreparedStatement updt = connexion
                                    .prepareStatement("UPDATE offres SET quantity = quantity + ? WHERE offer_uuid = ?");
                    updt.setDouble(1,-myQuantity);
                    updt.setString(2, offerUUID.toString());

                    if (updt.executeUpdate() != 1) {
                        return new JSONObject().put("err", 416);
                    }
                    pricePaid += otherPrice * myQuantity;
                    myQuantity = 0;
                }

                if (myQuantity <= 0) {
                    System.out.println("pricePaid = " + pricePaid);
                    return new JSONObject().put("err", 200);
                }

            }
            postOrder(id, true, currencies, myQuantity, myPrice);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    private static void moveOrderToLogs(UUID offerUUID) {
        Connection connexion = getConnexion();
        PreparedStatement updt = null;
        try {
            updt = connexion
                            .prepareStatement("UPDATE offres SET quantity = ? WHERE offer_uuid = ?");
            updt.setDouble(1,0.0);
            updt.setString(2, offerUUID.toString());

            if (updt.executeUpdate() != 1) {
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
