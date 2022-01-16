<%@ page import="java.util.Objects" %>
<%@ page import="domain.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" class="domain.ChessPlayer" scope="session"/>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>

<%
    ChessGame game = cgdm.find(Integer.parseInt(request.getParameter("game_id"))).get();
    try {
    
        String[] moveInput = request.getParameter("move").split(" ",0);

        if (Objects.equals(moveInput[0], "castle")) {
            int kingY = "WHITE".equals(request.getParameter("team")) ? 0 : 7;

            if (Objects.equals(moveInput[1], "long")) {
                ChessMove move = new ChessMove(("WHITE".equals(request.getParameter("team")) ? game.getWhite() : game.getBlack()),
                        new ChessPosition(kingY, 4), new ChessPosition(kingY, 2));
                move.setCastling(CastlingDirection.QUEEN_SIDE);
                game.addMove(move);
            } else if (Objects.equals(moveInput[1], "short")) {
                ChessMove move = new ChessMove(("WHITE".equals(request.getParameter("team")) ? game.getWhite() : game.getBlack()),
                        new ChessPosition(kingY, 4), new ChessPosition(kingY, 6));
                move.setCastling(CastlingDirection.KING_SIDE);
                game.addMove(move);
            }
        } else {
            char[] origin = moveInput[0].toCharArray();
            int originX = (int) origin[0] - (int) 'a';
            int originY = Character.getNumericValue(origin[1]) - 1;
            char[] destination = moveInput[1].toCharArray();
            int destinationX = (int) destination[0] - (int) 'a';
            int destinationY = Character.getNumericValue(destination[1]) - 1;

            ChessMove move = new ChessMove(("WHITE".equals(request.getParameter("team")) ? game.getWhite() : game.getBlack()),
                    new ChessPosition(originY, originX), new ChessPosition(destinationY, destinationX));
            game.addMove(move);
        }

    cgdm.update(game);
    response.sendRedirect(request.getContextPath() + "/game.jsp?id=" + game.getId());
    
} catch (IllegalMoveException e){ %>

<jsp:useBean id="moveerror" class="domain.MoveError" scope="request">
    <jsp:setProperty name="moveerror" property="message" value="Invalid move!"/>
</jsp:useBean>
<jsp:forward page="game.jsp?id=${param.game_id}"/>

<%
    } %>