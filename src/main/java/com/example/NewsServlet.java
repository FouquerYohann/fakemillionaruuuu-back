/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONArray;

import com.stuff.BloomAPI;

@WebServlet("/requestNews")
public class NewsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int nbNews = Integer.parseInt(req.getParameter("nbNews"));

        resp.setContentType("application/json");

        JSONArray retour = BloomAPI.requestNews(nbNews);

        resp.getOutputStream().print(retour.toString());
    }
}
