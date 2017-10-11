package com.example;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.http.*;


public class ShowUsersServlet extends HttpServlet {

    public ShowUsersServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("html/text");

        Scanner in = new Scanner(new File("src/main/resources/netflix-stock-price.csv"));
        String received ="";

        while (in.hasNext()) {
            received += in.nextLine();
            received+="\n";
        }

        resp.getOutputStream().print(received);
    }


}
