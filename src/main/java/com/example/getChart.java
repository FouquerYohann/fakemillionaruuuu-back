/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package com.example;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONArray;

import com.stuff.PoloAPI;

@WebServlet("/getChartData")
public class getChart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pairCurrency = req.getParameter("pairCurrency");
        String period = req.getParameter("period");
        long start = Long.parseLong(req.getParameter("start"));
        long end = Long.parseLong(req.getParameter("end"));

        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            System.out.println("Parameter Name - " + paramName +", Value - " + req.getParameter(paramName));
        }

        PoloAPI.CandlePeriod candlePeriod = PoloAPI.CandlePeriod.findCandlePeriod("FIVE_MINUTES");
        PoloAPI.CurrencyPair currencyPair = PoloAPI.CurrencyPair.valueOf("BTC_BCH");

        JSONArray retour = PoloAPI.requestCandleChart(currencyPair,Integer.parseInt(candlePeriod.toString()), start, end);

        resp.getOutputStream().print(retour.toString());
    }
}
