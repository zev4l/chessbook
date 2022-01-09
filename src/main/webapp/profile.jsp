<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" class="domain.ChessPlayer" scope="session"/>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
<%@ page import="domain.ChessGame" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.Color" %>
<% List<ChessGame> chessGameList = cgdm.findByPlayerEmail(user.getEmail()); %>
<html>
    <head>
        <title>ChessBook</title>
    </head>
    <body>
    <h1>ChessBook</h1>
    <h3>${user.name}</h3>
    <% if (!chessGameList.isEmpty()) { %>
        <h2>Current Games</h2>
        <table>
            <thead>
            <tr>
                <td>-</td>
                <td>White</td>
                <td>Time</td>
                <td>Black</td>
                <td>Time</td>
                <td>Last move</td>
                <td>--</td>
            </tr>
            </thead>
            <tbody>
            <% for(int i = 0; i < chessGameList.size(); i++) {
                ChessGame game = chessGameList.get(i);%>
                <tr>
                    <td><%=i+1%></td>
                    <td><%=game.getWhite().getName()%></td>
                    <td><%=game.totalTime(Color.WHITE)%></td>
                    <td><%=game.getBlack().getName()%></td>
                    <td><%=game.totalTime(Color.BLACK)%></td>
                    <% if(game.getWinner() == null) {%>
                    <td><%=game.getTurn().opposite%></td>
                    <% if(game.getWhite().getName().equals(user.getName()) && game.getTurn() == Color.WHITE) { %>
                    <td><form action="game.jsp" method="get">
                        <input type="hidden" name="id" value="<%=game.getId()%>">
                        <input type="submit" value="Play">
                    </form> </td>
                    <% } else { %>
                    <td><%=game.getTimestamp()%></td>
                    <% } %>
                    <% } else { %>
                    <td><%=game.getOutcome()%></td>
                    <td>
                        <form method="get" action="processPlay.jsp">
                            <%if(game.getWhite().getName().equals(user.getName())) {%>
                            <input type="hidden" name="name" value="<%=game.getBlack().getName()%>">
                            <%} else {%>
                            <input type="hidden" name="name" value="<%=game.getWhite().getName()%>">
                            <%}%>
                            <input type="submit" value="Replay">
                        </form>
                    </td>
                    <% } %>
                </tr>
            <% } %>
            </tbody>
        </table>
    <% } %>
    <h2>Start a new game</h2>
    <form method="GET" action="processPlay.jsp">
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
