/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.stuff.DBUtils;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login =req.getParameter("login");

        DBUtils.closeSession(login);
    }
}
