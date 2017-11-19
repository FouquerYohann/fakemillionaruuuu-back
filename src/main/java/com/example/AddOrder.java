/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package com.example;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.stuff.WalletUtils;

@WebServlet("/addOrder")
public class AddOrder extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");


        Enumeration<String> params = req.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            System.out.println("Parameter Name - "+paramName+", Value - "+req.getParameter(paramName));
        }

        int id = Integer.parseInt(req.getParameter("id"));
        boolean buy = Boolean.parseBoolean(req.getParameter("buy"));
        String currency = req.getParameter("currency");
        double price = Double.parseDouble(req.getParameter("price"));
        double quantity = Double.parseDouble(req.getParameter("quantity"));

        resp.getOutputStream().print(WalletUtils.postOrder(id, buy, currency, quantity, price).toString());
    }
}
