<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" class="domain.ChessPlayer" scope="session"/>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
<%@ page import="domain.ChessGame" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="domain.ChessPlayer" %>
<%@ page import="java.util.Collections" %>
<html>
<head>
    <title>ChessBook</title>
    <link rel="stylesheet" type="text/css" href="styles/styles.css">
</head>
<%  ChessPlayer userPlayer = cpdm.findByEmail(user.getEmail()).get();
    ChessPlayer queryPlayer = cpdm.find(Integer.parseInt(request.getParameter("id"))).get();
    if(!(userPlayer.getId() == queryPlayer.getId())) {
        List<ChessPlayer> chessPlayers = new ArrayList<>();
        chessPlayers.add(cpdm.findByEmail(user.getEmail()).get());
        chessPlayers.add(cpdm.find(Integer.parseInt(request.getParameter("id"))).get());
    
        Collections.shuffle(chessPlayers);
        ChessGame game = new ChessGame(chessPlayers.get(0), chessPlayers.get(1), new java.util.Date());
        cgdm.insert(game);
        response.sendRedirect(request.getContextPath() + "/game.jsp?id=" + game.getId());
    } else {%>

<body>
<h1>ChessBook</h1>
<h2>Creating game failed!</h2>
<h3>You can't start a game with yourself.</h3>
<p>
    You may:<br>
    <a href="profile.jsp">Try Again</a>
</p>
<p>
<%= new java.util.Date() %>
</p>

<p>
    <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
</p>
</body>
</html>
<%
    }
%>