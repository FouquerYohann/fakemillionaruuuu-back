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
import com.stuff.PoloAPI.CandlePeriod;
import com.stuff.PoloAPI.CurrencyPair;

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

        CandlePeriod candlePeriod = CandlePeriod.valueOf(period);
        CurrencyPair currencyPair = CurrencyPair.valueOf(pairCurrency);

        JSONArray retour = PoloAPI.requestCandleChart(CurrencyPair.BTC_BCH,300, start, end);

        resp.getOutputStream().print(retour.toString());
    }
}
