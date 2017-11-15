package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

import com.stuff.DBUtils;

/**
 *Created by Yohann on 24/10/2017.
 */

@WebServlet("/pay")
public class Pay extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        int from = Integer.parseInt(req.getParameter("from"));
        int to = Integer.parseInt(req.getParameter("to"));
        String currency = req.getParameter("what");
        int value = Integer.parseInt(req.getParameter("value"));

        JSONObject take = DBUtils.changeValue(from, currency, -value);
        if (200 == take.getInt("err")) {
            JSONObject give = DBUtils.changeValue(to, currency, value);
        }
        resp.getOutputStream().print(new JSONObject().put("err", 200).toString());
    }
}

