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

<c:if test="${!(empty moveerror)}">
    <p>${moveerror.message}</p>
</c:if>

<% if (!game.isOver()){
    if((game.getWhite().equals(user) && game.getTurn() == Color.WHITE ) || (game.getBlack().equals(user) && game.getTurn() == Color.BLACK)){

        // If it's the first turn, set firstOpenedTimestamp at launch
        if (game.getMoves().isEmpty()) {
            game.setFirstOpenedTimestamp(new Date());
            cgdm.update(game);
        } else {
            // If the last move hadn't been seen before, set seenTimestamp
            if (!game.getLastMove().hasBeenSeen()) {
                game.getLastMove().setSeenTimestamp(new Date());
                cgdm.update(game);
            }
        }
%>

<form action="processMove.jsp" method="post">
    <label for="moveInput">Your move:</label>
    <input name="move" id="moveInput" type="text">
    <input type="hidden" name="game_id" value="<%=game.getId()%>">
    <input type="hidden" name="team" value="<%=game.getTurn()%>">
    <input type="submit" value="Submit">
</form>

<% } else { %>
<p> It's <%=game.getTurn() == Color.WHITE ? game.getWhite().getName() : game.getBlack().getName()%>'s turn.</p>

<%}
} else {%>

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
