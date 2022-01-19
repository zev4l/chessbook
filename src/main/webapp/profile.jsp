<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" class="domain.ChessPlayer" scope="session"/>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
<%@ page import="domain.ChessGame" %>
<%@ page import="java.util.List" %>
<%@ page import="domain.Color" %>
<%@ page import="java.time.Duration" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Objects" %>
<% cgdm = cgdm.getInstance();%>
<% List<ChessGame> chessGameList = cgdm.findByPlayerEmail(user.getEmail()); %>
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

            <h3 class="h3 my-3 font-weight-normal">${user.name}'s profile</h3>
            <% if (!chessGameList.isEmpty()) { %>
            <h2>Current Games</h2>



            <table class="table table-hover my-3">
                <caption>
                    <i class="fas fa-circle success-caption"></i>Win<br>
                    <i class="fas fa-circle danger-caption"></i>Loss<br>
                    <i class="fas fa-circle warning-caption"></i>Draw<br>

                </caption>
                <thead>
                <tr>
                    <td>#</td>
                    <td>White</td>
                    <td>Time Left</td>
                    <td>Black</td>
                    <td>Time Left</td>
                    <td>Last move</td>
                    <td>Action</td>
                </tr>
                </thead>
                <tbody>
                <% for(int i = 0; i < chessGameList.size(); i++) {
                    ChessGame game = chessGameList.get(i);
                    Duration whiteTimeLeft = game.getTotalTimeLeft(Color.WHITE);
                    Duration blackTimeLeft = game.getTotalTimeLeft(Color.BLACK);

                    String rowClass = "";
                    if (game.isOver()) {
                        if (game.getWinner() != null) {
                            if (game.getWinner() == Color.WHITE && Objects.equals(game.getWhite().getEmail(), user.getEmail()) || game.getWinner() == Color.BLACK && Objects.equals(game.getBlack().getEmail(), user.getEmail())) {
                                rowClass = "table-success";
                            } else {
                                rowClass = "table-danger";
                            }
                        } else {
                            rowClass = "table-warning";
                        }
                    }

                %>
                <tr class="<%=rowClass%>">
                    <td><%=i+1%></td>
                    <td><%=game.getWhite().getName()%></td>
                    <td><%=String.format("%02d:%02d", whiteTimeLeft.toMinutes(), whiteTimeLeft.toSecondsPart())%></td>
                    <td><%=game.getBlack().getName()%></td>
                    <td><%=String.format("%02d:%02d", blackTimeLeft.toMinutes(), blackTimeLeft.toSecondsPart())%></td>

                    <% if (!game.isOver()) {%>

                    <td><%=game.getTurn().opposite.toString().substring(0,1).toUpperCase() + game.getTurn().opposite.toString().substring(1).toLowerCase() + " to play."%></td>
                    <% if((game.getWhite().getEmail().equals(user.getEmail()) && game.getTurn() == Color.WHITE ) || (game.getBlack().getEmail().equals(user.getEmail()) && game.getTurn() == Color.BLACK)) { %>
                    <td><form action="game.jsp" method="get">
                        <input type="hidden" name="id" value="<%=game.getId()%>">
                        <button type="submit" class="btn btn-sm btn-primary btn-block">Play</button>
                    </form> </td>
                    <% } else { %>
                    <td><%=game.getTimestamp()%></td>
                    <% } %>
                    <% } else { %>
                    <td><%=String.format("%s won by %s.", game.getWinner() == Color.WHITE ? game.getWhite().getName() : game.getBlack().getName(), game.getOutcome().toString().toLowerCase())%></td>
                    <td>
                        <form method="get" action="replay.jsp">
                            <%if(game.getWhite().getName().equals(user.getName())) {%>
                            <input type="hidden" name="name" value="<%=game.getBlack().getName()%>">
                            <%} else {%>
                            <input type="hidden" name="name" value="<%=game.getWhite().getName()%>">
                            <%}%>
                            <input type="hidden" name="id" value="<%=game.getId()%>">
                            <input type="hidden" name="moveIdx" value="<%=game.getMoves().size()-1%>">
                            <button type="submit" class="btn btn-sm btn-primary btn-block">Replay</button>
                        </form>
                    </td>
                    <% } %>
                </tr>
                <% } %>
                </tbody>
            </table>
            <% } %>
            <h2>Start a new game</h2>
            <form method="GET" action="processPlay.jsp" class="form-container">
                <p>Enter the username of your opponent:</p>
                <div class="d-flex">
                    <div class="form-group mr-auto">
                        <input type="text" name="name" id="inputName" class="form-control" placeholder="Username">
                    </div>
                    <div class="form-group mx-2 ml-auto">
                        <button class="btn btn-md btn-primary btn-block" type="submit">Play</button>
                    </div>
                </div>
            </form>
            <p class="mt-2 mb-3 text-muted"><%= new java.util.Date()%></p>
            <p>
                <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
            </p>

        </div>
    </div>

    </body>
</html>
