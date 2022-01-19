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
<% ChessGame game = cgdm.find(Integer.parseInt(request.getParameter("id"))).get();%>

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
                        ChessPiece piece = game.getBoard().get(y, x);

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


        <c:if test="${!(empty moveerror)}">
            <h5 class="mt-3">${moveerror.message}</h5>
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

        <form action="processMove.jsp" method="post" class="form-container">
            <input type="hidden" name="game_id" value="<%=game.getId()%>">
            <input type="hidden" name="team" value="<%=game.getTurn()%>">
            <div class="d-flex align-items-center">
                <div class="form-group mr-auto">
                    <input name="move" id="moveInput" type="text" placeholder="Your move" autocomplete="off">
                </div>
                <div class="form-group mx-2 ml-auto">
                    <button class="btn btn-md btn-primary btn-block" type="submit">Play</button>
                </div>
            </div>

        </form>

        <% } else { %>
        <h5> It's <%=game.getTurn() == Color.WHITE ? game.getWhite().getName() : game.getBlack().getName()%>'s turn.</h5>

        <%}
        } else {%>

        <h5>
            Good game! Outcome:
            <% if (game.getOutcome() == Outcome.CHECKMATE || game.getOutcome() == Outcome.RESIGNATION || game.getOutcome() == Outcome.TIMEOUT) { %>
            <%=(game.getWinner() == Color.BLACK) ? game.getBlack().getName() : game.getWhite().getName() %> won by <%= game.getOutcome().toString().toLowerCase() %>!
            <% } else { %>
            Draw!
            <% } %>
        </h5>

        <% } %>

        <p class="mt-2 mb-3 text-muted"><%= new java.util.Date()%></p>

        <p>
            <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
        </p>

    </div>
</div>

</body>
</html>
