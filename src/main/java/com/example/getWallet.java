package com.example;

import static com.stuff.DBUtils.getWalletValue;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/getWallet")
public class getWallet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        int personid = Integer.parseInt(req.getParameter("personId"));

        resp.getOutputStream().print(getWalletValue(personid).toString());
    }
}
