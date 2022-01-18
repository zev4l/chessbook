<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="domain.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" class="domain.ChessPlayer" scope="session"/>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
<jsp:useBean id="moveerror" class="domain.MoveError" scope="request"/>

<% cgdm = cgdm.getInstance();%>
<% ChessGame game = cgdm.find(Integer.parseInt(request.getParameter("id"))).get();
   int moveToDisplay = Math.max(0, Math.min(Integer.parseInt(request.getParameter("moveIdx")), game.getMoves().size() - 1));

   ChessBoard boardAtMove = new ChessBoard();
   boardAtMove.rebuildBoard(game.getMoves().subList(0, moveToDisplay + 1));
%>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="styles/styles.css">
    <title>ChessBook</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
</head>
<body>

<h1>ChessBook</h1>

<h3></h3>

<p>
    <%=game.getWhite().getName()%> (white) vs <%=game.getBlack().getName()%> (black)
</p>

<div class="chessboard">
    <pre><%=boardAtMove.toString()%></pre>
</div>

<div class="button-holder">
    <form method="get" action="replay.jsp" id="previousMove">
        <input type="hidden" name="id" value="<%=game.getId()%>">
        <input type="hidden" name="moveIdx" value="<%=moveToDisplay - 1%>">

    </form>
    <form method="get" action="replay.jsp" id="nextMove">
        <input type="hidden" name="id" value="<%=game.getId()%>">
        <input type="hidden" name="moveIdx" value="<%=moveToDisplay + 1%>">
    </form>
    <button type="submit" form="previousMove" value="Submit"><a><i class="fas fa-angle-left"></i></a></button>
    <button type="submit" form="nextMove" value="Submit"><a><i class="fas fa-angle-right"></i></a></button>
</div>

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
