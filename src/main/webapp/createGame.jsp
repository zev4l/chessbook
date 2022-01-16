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

<% List<ChessPlayer> chessPlayers = new ArrayList<>();
    chessPlayers.add(cpdm.findByName(user.getName()).get(0));
    chessPlayers.add(cpdm.find(Integer.parseInt(request.getParameter("id"))).get());
    Collections.shuffle(chessPlayers);
    ChessGame game = new ChessGame(chessPlayers.get(0), chessPlayers.get(1), new java.util.Date());
    cgdm.insert(game);
%>

<%response.sendRedirect(request.getContextPath() + "/game.jsp?id=" + cgdm.chessGamesList().get(cgdm.chessGamesList().size()-1).getId());%>