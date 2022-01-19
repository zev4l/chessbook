<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="domain.*" %>
<%@ page import="persist.ChessGameDM" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" class="domain.ChessPlayer" scope="session"/>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
<jsp:useBean id="moveerror" class="domain.MoveError" scope="request"/>

<% cgdm = ChessGameDM.getInstance();%>
<% ChessGame game = cgdm.find(Integer.parseInt(request.getParameter("id"))).get();

    int moveToDisplay = Math.max(0, Math.min(Integer.parseInt(request.getParameter("moveIdx")), game.getMoves().size() - 1));

    ChessBoard boardAtMove = new ChessBoard();
    boardAtMove.rebuildBoard(game.getMoves().subList(0, moveToDisplay + 1));
%>

<html>
<head>
    <title>ChessBook</title>
    <link rel="stylesheet" type="text/css" href="styles/styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link rel="icon" href="styles/icon.png">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
</head>
<body class="text-center vh-100">

<div class="d-flex justify-content-center align-items-center">
    <div class="container">
        <h1 class="h1 mb-3 font-weight-normal">ChessBook</h1>
        <img class="my-3 logo" src="styles/icon.png" alt="" width="72">


        <h3 class="my-3">
            <%=game.getWhite().getName()%> (white) vs <%=game.getBlack().getName()%> (black)
        </h3>

        <div class="chessboard">
            <%
                for (int y = 7; y >= 0; y--) {
                    for (int x = 0; x < 8; x++) {
                        ChessPiece piece = boardAtMove.get(y, x);

                        String pieceClass = "";

                        if (piece != null) {
                            pieceClass = "fas fa-2x ";

                            if (piece.getColor() == Color.WHITE) {
                                pieceClass += "whitePiece ";
                            } else {
                                pieceClass += "blackPiece ";
                            }

                            if (piece.getChessPieceKind() == ChessPieceKind.PAWN) {
                                pieceClass += "fa-chess-pawn";

                            } else if (piece.getChessPieceKind() == ChessPieceKind.QUEEN) {
                                pieceClass += "fa-chess-queen";

                            } else if (piece.getChessPieceKind() == ChessPieceKind.ROOK) {
                                pieceClass += "fa-chess-rook";

                            } else if (piece.getChessPieceKind() == ChessPieceKind.BISHOP) {
                                pieceClass += "fa-chess-bishop";

                            } else if (piece.getChessPieceKind() == ChessPieceKind.KNIGHT) {
                                pieceClass += "fa-chess-knight";

                            } else if (piece.getChessPieceKind() == ChessPieceKind.KING) {
                                pieceClass += "fa-chess-king";
                            }
                        }

            %>

                     <div><i class="<%=pieceClass%>"></i>
                         <% if (x == 0) {
                             %> <span class="rowIndicator"> <%=y+1%> </span> <%
                         }
                         if (y == 0) {
                             %> <span class="colIndicator"> <%=(char) ('a' + x) %> </span> <%
                         } %>
                     </div>

                    <% }} %>
        </div>

        <div class="button-holder">
            <form method="get" action="replay.jsp" id="previousMove">
                <input type="hidden" name="id" value="<%=game.getId()%>">
                <input type="hidden" name="moveIdx" value="<%=moveToDisplay - 1 < 0 ? game.getMoves().size() - 1 : moveToDisplay - 1 %>">

            </form>
            <form method="get" action="replay.jsp" id="nextMove">
                <input type="hidden" name="id" value="<%=game.getId()%>">
                <input type="hidden" name="moveIdx" value="<%= moveToDisplay + 1 == game.getMoves().size() ? 0 : moveToDisplay + 1 %>">
            </form>
            <button class="btn btn-lg mb-3" type="submit" form="previousMove" value="Submit"><a><i class="fas fa-angle-left fa-lg"></i></a></button>
            <button class="btn btn-lg mb-3" type="submit" form="nextMove" value="Submit"><a><i class="fas fa-angle-right fa-lg"></i></a></button>
        </div>

        <% if (!game.isOver()){
        %>
        <h5> It's <%=game.getTurn() == Color.WHITE ? game.getWhite().getName() : game.getBlack().getName()%>'s turn.</h5>

        <% } else { %>
        <h5>
            Good game! Outcome:
            <% if (game.getOutcome() == Outcome.CHECKMATE || game.getOutcome() == Outcome.RESIGNATION || game.getOutcome() == Outcome.TIMEOUT) { %>
            <%=(game.getWinner() == Color.BLACK) ? game.getBlack().getName() : game.getWhite().getName() %> (<%=game.getWinner().toString().toLowerCase()%>) won by <%= game.getOutcome().toString().toLowerCase() %>!
            <% } else { %>
            Draw!
            <% } %>
        </h5>

        <% } %>

        <p class="mt-2 mb-3 text-muted"><%= new java.util.Date()%></p>

        <p>
            <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
        </p>

    </div></div>
</body>
</html>
