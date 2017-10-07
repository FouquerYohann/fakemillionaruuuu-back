<%@ page import="main.com.stuff.UserService" %>
<%@ page import="java.sql.*" %>
<html>
<body>
<h2>Hello World!</h2>
</body>
<%
    out.print(System.getenv("JDBC_DATABASE_URL"));
%>
</html>
