/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package com.example;

import static com.stuff.DBUtils.login;

import java.io.IOException;
import java.util.Enumeration;
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

        Enumeration<String> params = req.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = params.nextElement();
            System.out.println("Parameter Name - "+paramName+", Value - "+req.getParameter(paramName));
        }




//        JSONObject jsonObject = new JSONObject(req.getParameter("request"));
        JSONObject jsonObject2 = new JSONObject(req.getParameter("data"));

        System.out.println("request    " + jsonObject2);
        System.out.println("data    " + jsonObject2);



        String login = (String) jsonObject2.get("login");
        String password = (String) jsonObject2.get("password");
        resp.setContentType("application/json");

        JSONObject reponse = login(login, password);
        resp.getOutputStream().print(reponse.toString());

    }
}


