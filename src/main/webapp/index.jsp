<%@ page import="main.com.stuff.DBUtils" %>
<%@ page import="java.sql.*" %>
<html>
<body>
<h2>Hello World!</h2>
</body>
<%
    out.print(System.getenv("JDBC_DATABASE_URL"));
%>
<%
    Connection connexion = DBUtils.getConnexion();
    Statement statement = connexion.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM Users;");
    out.println("<table>");
    while (resultSet.next()) {
        out.println("<tr>");
        out.println("<td>" + resultSet.getString("PersonID") + "</td");
        out.println("<td>" + resultSet.getString("Login") + "</td");
        out.println("<td>" + resultSet.getString("Password") + "</td");
        out.println("</tr>");
    }
    out.println("</table>");
%>
</html>
