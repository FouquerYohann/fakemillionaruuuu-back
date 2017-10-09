package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;


public class ShowUsersServlet extends HttpServlet {

    public ShowUsersServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getOutputStream().print("pas d'implementation prevu uniquement pour le test");

        //        resp.setContentType("text/html");
//        ServletOutputStream outputStream = resp.getOutputStream();
//        resp.addHeader("ICI","c'est la !!");
//        outputStream.print("YOOO");
//
//        try {
//            Connection connexion = DBUtils.getConnexion();
//            Statement statement = connexion.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM Users;");
//            outputStream.println("<table>");
//            while (resultSet.next()) {
//                outputStream.println("<tr>");
//                outputStream.println("<td>" + resultSet.getString("PersonID") + "</td>");
//                outputStream.println("<td>" + resultSet.getString("Login") + "</td>");
//                outputStream.println("<td>" + resultSet.getString("Password") + "</td>");
//                outputStream.println("</tr>");
//            }
//            outputStream.println("</table>");
//
////            req.getRequestDispatcher("users.jsp").forward(req, resp);
//        } catch (SQLException e) {
//            outputStream.print(System.getenv("JDBC_DATABASE_URL"));
//            outputStream.print(e.getMessage());
//        }
    }


}
