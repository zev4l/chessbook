<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" class="domain.ChessPlayer" scope="session"/>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
<html>
    <head>
        <title>ChessBook</title>
    </head>
    <body>
    <h1>ChessBook</h1>
    <h3>${user.name}</h3>
    <h2>Start a new game</h2>
    <form method="GET" action="findOpponent.jsp">
        Enter the name of your opponent:<br>
        <label for="inputName">Name: </label>
        <input type="text" name="name" id="inputName">
        <input type="submit" value="Play">
    </form>
    <p>
    <%= new java.util.Date()%>
    </p>
    <p>
        <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
    </p>
    </body>
</html>
