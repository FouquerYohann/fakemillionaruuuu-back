/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package main.tests;

import java.sql.*;

import main.com.stuff.DBUtils;

public class MainTrifouille {

    public static void main(String[] args) {
        try {

            Connection connexion = DBUtils.getConnexion();
            Statement statement = connexion.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users;");
            System.out.println("<table>");
            while (resultSet.next()) {
                System.out.println("<tr>");
                System.out.println("<td>" + resultSet.getString("PersonID") + "</td");
                System.out.println("<td>" + resultSet.getString("Login") + "</td");
                System.out.println("<td>" + resultSet.getString("Password") + "</td");
                System.out.println("</tr>");
            }
            System.out.println("</table>");
            statement = connexion.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
