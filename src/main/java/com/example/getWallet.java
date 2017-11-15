package com.example;

import static com.stuff.DBUtils.getWalletValue;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

@WebServlet("/getWallet")
public class getWallet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String personId = req.getParameter("personId");
        if (personId.isEmpty()) {
            resp.getOutputStream().print(new JSONObject().put("err", 415).toString());
        }
        int personid = Integer.parseInt(personId);


        resp.getOutputStream().print(getWalletValue(personid).toString());
    }
}
