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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link rel="icon" href="styles/icon.png">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
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

<body class="text-center vh-100">
<div class="d-flex justify-content-center align-items-center">
    <div class="container">
        <h1 class="h1 mb-3 font-weight-normal">ChessBook</h1>
        <img class="my-3 logo" src="styles/icon.png" alt="" width="72">

        <h2>Creating game failed!</h2>
        <h3>You can't start a game with yourself.</h3>
        <a href="profile.jsp" class=" my-3 btn btn-sm btn-primary btn-block" role="button">Try again</a>
        <p class="mt-2 mb-3 text-muted"><%= new java.util.Date()%></p>


        <p>
            <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
        </p>
    </div>
</div>
</body>
</html>
<%
    }
%>