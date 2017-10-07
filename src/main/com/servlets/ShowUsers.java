package main.com.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;


public class ShowUsers extends HttpServlet {

    public ShowUsers() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        ServletOutputStream outputStream = resp.getOutputStream();

        outputStream.print("YOOO");
        req.getRequestDispatcher("/index.jsp").forward(req, resp);

//                try {
//                    Connection connexion = UserService.getConnexion();
//                    Statement statement = connexion.createStatement();
//                    ResultSet resultSet = statement.executeQuery("SELECT * FROM Users;");
//                    outputStream.println("<table>");
//                    while (resultSet.next()) {
//                        outputStream.println("<tr>");
//                        outputStream.println("<td>" + resultSet.getString("PersonID") + "</td");
//                        outputStream.println("<td>" + resultSet.getString("Login") + "</td");
//                        outputStream.println("<td>" + resultSet.getString("Password") + "</td");
//                        outputStream.println("</tr>");
//                    }
//                    outputStream.println("</table>");
//
//                    req.getRequestDispatcher("/index.jsp").forward(req, resp);
//                } catch (SQLException e) {
//                    outputStream.print(System.getenv("JDBC_DATABASE_URL"));
//                    outputStream.print(e.getMessage());
//                }
    }
}
