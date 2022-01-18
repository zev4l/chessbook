<%@ page import="domain.ChessGame" %>
<%@ page import="domain.Color" %>
<%@ page import="domain.Outcome" %>
<%@ page import="domain.ChessPiece" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" class="domain.ChessPlayer" scope="session"/>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
<jsp:useBean id="moveerror" class="domain.MoveError" scope="request"/>

<% cgdm = cgdm.getInstance();%>
<% ChessGame game = cgdm.find(Integer.parseInt(request.getParameter("id"))).get();%>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="styles/styles.css">
    <title>ChessBook</title>
</head>
<body>

<h1>ChessBook</h1>

<h3></h3>

<p>
    <%=game.getWhite().getName()%> (white) vs <%=game.getBlack().getName()%> (black)
</p>

<div class="chessboard">
</div>

<pre><%=game.getBoard().toString()%></pre>

<% if (!game.isOver()){
%>
<p> It's <%=game.getTurn() == Color.WHITE ? game.getWhite().getName() : game.getBlack().getName()%>'s turn.</p>

<% } else { %>
<p>
    Good game! Outcome:
    <% if (game.getOutcome() == Outcome.CHECKMATE || game.getOutcome() == Outcome.RESIGNATION || game.getOutcome() == Outcome.TIMEOUT) { %>
    <%=(game.getWinner() == Color.BLACK) ? game.getBlack().getName() : game.getWhite().getName() %> won by <%= game.getOutcome().toString().toLowerCase() %>!
    <% } else { %>
    Draw!
    <% } %>
</p>

<% } %>

<p>
    <%= new java.util.Date() %>
</p>

<p>
    <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
</p>

</body>
</html>
