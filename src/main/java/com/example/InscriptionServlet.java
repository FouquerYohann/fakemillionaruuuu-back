/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package com.example;

import static com.stuff.DBUtils.inscription;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String login = req.getParameter("login");
        String password = req.getParameter("password");
//        String mail = req.getParameter("mail");

        JSONObject retour = inscription(login, password);
        resp.getOutputStream().print(retour.toString());

    }
}
