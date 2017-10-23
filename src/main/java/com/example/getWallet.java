package com.example;

import com.stuff.DBUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.stuff.DBUtils.getWalletValue;

@WebServlet("/getWallet")
public class getWallet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int personid = Integer.parseInt(req.getParameter("personId"));

        resp.getOutputStream().print(getWalletValue(personid).toString());
    }
}
