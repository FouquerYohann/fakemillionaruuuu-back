/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.stuff.WalletUtils;

public class AddOrder extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        int id = Integer.parseInt(req.getParameter("id"));
        boolean buy = Boolean.parseBoolean(req.getParameter("buy"));
        String currency = req.getParameter("currency");
        double price = Double.parseDouble(req.getParameter("price"));
        double quantity = Double.parseDouble(req.getParameter("quantity"));

        resp.getOutputStream().print(WalletUtils.postOrder(id, buy, currency, quantity, price).toString());
    }
}
