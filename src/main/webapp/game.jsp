<%@ page import="domain.ChessGame" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" class="domain.ChessPlayer" scope="session"/>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
<% ChessGame game = cgdm.find(Integer.parseInt(request.getParameter("id"))).get();%>
<jsp:useBean id="moveerror" scope="request" type="domain.MoveError"/>
<html>
<head>
    <title>ChessBook</title>
</head>
<body>
<h1>ChessBook</h1>
<h3></h3>
<p>
    <%=game.getWhite().getName()%> (white) vs <%=game.getBlack().getName()%> (black)
</p>
<p><%=game.getBoard().toString()%></p>
<c:if test="${!(empty moveerror)}">
    <p>${moveerror.message}</p>
</c:if>
<form action="processMove.jsp" method="post">
    <label for="moveInput">Your move:</label>
    <input name="move" id="moveInput" type="text">
    <input type="hidden" name="game_id" value="<%=game.getId()%>">
    <input type="hidden" name="team" value="<%=game.getTurn()%>">
    <input type="submit" value="Submit">
</form>
</body>
</html>
