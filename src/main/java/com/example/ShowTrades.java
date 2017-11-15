/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package com.example;

import static com.stuff.PoloAPI.CURRENCIES.valueOf;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.stuff.PoloAPI.CURRENCIES;
import com.stuff.WalletUtils;

@WebServlet("/showtrades")
public class ShowTrades extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        CURRENCIES currency = valueOf(req.getParameter("currency"));

        resp.getOutputStream().print(WalletUtils.getTrades(currency).toString());

    }
}
