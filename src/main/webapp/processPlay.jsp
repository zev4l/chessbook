<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" class="domain.ChessPlayer" scope="session"/>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
<%@ page import="java.util.List" %>
<%@ page import="domain.ChessPlayer" %>
<c:choose>
    <c:when test="${cpdm.findByName(param.name).size() eq 1}">
        <%response.sendRedirect(request.getContextPath() + "/createGame.jsp?id=" + cpdm.findByName(request.getParameter("name")).get(0).getId());%>
    </c:when>
    <c:when test="${cpdm.findByName(param.name).size() gt 1}">
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
                <h3>${user.name}</h3>

                <h4 class="mt-5">Search for "<%= request.getParameter("name")%>"</h4>
                <table class="table table-hover my-3 w-25">
                    <thead>
                    <tr>
                        <td>Name</td>
                        <td>Email</td>
                        <td>Choice</td>
                    </tr>
                    </thead>
                    <tbody>
                    <% List<ChessPlayer> query = cpdm.findByName(request.getParameter("name"));
                        for (ChessPlayer chessPlayer : query) {%>
                    <tr>
                        <td><%=chessPlayer.getName()%>
                        </td>
                        <td><%=chessPlayer.getEmail()%>
                        </td>
                        <td>
                            <form method="get" action="createGame.jsp">
                                <input type="hidden" name="id" value="<%=chessPlayer.getId()%>">
                                <button type="submit" class="btn btn-sm btn-primary btn-block">Start</button>
                            </form>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
                <p class="mt-2 mb-3 text-muted"><%= new java.util.Date()%></p>

                <p>
                    <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
                </p>
            </div>
        </div>
        </body>
        </html>
    </c:when>
    <c:otherwise>
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
                <h3>${user.name}</h3>
        <h4 class="mt-5">No users with name "<%=request.getParameter("name")%>" were found.</h4>

                <a href="profile.jsp" class=" my-3 btn btn-sm btn-primary btn-block" role="button">Try again</a>

                <p class="mt-2 mb-3 text-muted"><%= new java.util.Date()%></p>
        <p>
            <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
        </p>


            </div>
        </div>
        </body>
        </html>
    </c:otherwise>
</c:choose>