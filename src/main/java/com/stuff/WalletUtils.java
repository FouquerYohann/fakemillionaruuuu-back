package com.stuff;

import com.stuff.PoloAPI.CURRENCIES;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.stuff.DBUtils.DATE_TIME_FORMATTER;
import static com.stuff.DBUtils.getConnexion;
import static java.time.LocalDateTime.now;

/**
 * Created by Yohann on 26/10/2017.
 */
public class WalletUtils {

    public static JSONObject postOrder(int id, boolean buy, CURRENCIES currency, double quantity, double price) {
        Connection connection = DBUtils.getConnexion();

        try {
            PreparedStatement insert = connection
                    .prepareStatement("INSERT INTO offres (personid,buy,currency,quantity,price,time_start) " +
                            "        VALUES (?, ?, ?, ?, ?, ?);");
            insert.setInt(1, id);
            insert.setBoolean(2, buy);
            insert.setString(3, currency.toString());
            insert.setDouble(4, quantity);
            insert.setDouble(5, price);
            insert.setString(6, DATE_TIME_FORMATTER.format(now()));

            if (insert.executeUpdate() == 1) {
                return new JSONObject().put("err", 200);
            }
            return new JSONObject().put("err", 417);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JSONObject().put("err", 417);
    }


    public static JSONObject checkForSales(int id, boolean buy, CURRENCIES currencies, double quantity, double price) {
        Connection connection = getConnexion();

        try {
            PreparedStatement query = connection
                    .prepareStatement("SELECT * FROM offres where currency = ? AND price <= ? AND buy = ?");
            query.setString(1, currencies.toString());
            query.setDouble(2, price);
            query.setBoolean(3, !buy);


            ResultSet resultSet = query.executeQuery();

            while (resultSet.next() && quantity >0) {
                double otherQuantity = resultSet.getDouble("quantity");
                if (quantity < otherQuantity) {

                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
