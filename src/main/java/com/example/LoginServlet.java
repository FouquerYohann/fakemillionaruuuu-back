/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package com.example;

import static com.stuff.DBUtils.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           doPost(req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String login =req.getParameter("login");
        String password =req.getParameter("password");
        resp.setContentType("application/json");

        JSONObject reponse = login(login, password);
        resp.getOutputStream().print(reponse.toString());

    }
}


