<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>ChessBook</title>
    </head>
    <body>
        <h1>ChessBook</h1>
        <h2>ChessBook Login</h2>
        <form method="POST" action="processLogin.jsp">
            <label for="inputName">Name: </label>
            <input type="text" name="name" id="inputName">
            <br>
            <label for="inputEmail">Email:</label>
            <input type="text" name="email" id="inputEmail">
            <br>
            <input type="submit" value="Submit">
        </form>
        <p>or</p>
        <p><a href="register.jsp"> Register </a></p>
        <%= new java.util.Date()%>
    </body>
</html>
