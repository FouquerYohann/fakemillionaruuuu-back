package com.stuff;

import static com.stuff.DBUtils.DATE_TIME_FORMATTER;
import static com.stuff.DBUtils.getConnexion;
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

    public static JSONObject checkForSales(int id, boolean buy, CURRENCIES currencies, double myQuantity, double myPrice) {
        Connection connection = getConnexion();

        try {
            PreparedStatement query = connection
                            .prepareStatement("SELECT * FROM offres where currency = ? AND price <= ? AND buy = ?");
            query.setString(1, currencies.toString());
            query.setDouble(2, myPrice);
            query.setBoolean(3, !buy);

            ResultSet resultSet = query.executeQuery();

            while (resultSet.next() && myQuantity > 0) {
                double otherQuantity = resultSet.getDouble("quantity");
                double otherPrice = resultSet.getDouble("price");
                double otherTotalPrice = otherQuantity * otherPrice;
                double myTotalPrice = myPrice * myQuantity;
                String offer_uuid;
                if (myTotalPrice < otherTotalPrice) {
                    double quantityBought = myTotalPrice / otherPrice;

                    offer_uuid = resultSet.getString("offer_UUID");
                    PreparedStatement upd = connection
                                    .prepareStatement("UPDATE offres SET quantity = offres.quantity + ? WHERE " +
                                                    "offer_uuid = ?;");
                    upd.setDouble(1, -quantityBought);
                    upd.setString(2, offer_uuid.toString());
                    upd.executeUpdate();
                    //TODO
                    WalletUtils.moveOrderToLogs();
                } else {


                    myQuantity -= otherQuantity;
                    myTotalPrice -=otherTotalPrice;
                    //TODO
                    WalletUtils.moveOrderToLogs();
                }
            }
            if (myQuantity > 0) {
                postOrder(id, buy, currencies, myQuantity, myPrice);
            } else {
                WalletUtils.moveOrderToLogs();
            }
            return new JSONObject().put("err", 200);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JSONObject().put("err", 417);
    }

    private static void moveOrderToLogs() {

    }

}
