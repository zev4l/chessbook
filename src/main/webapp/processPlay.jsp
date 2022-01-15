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
        </head>
        <body>
        <h1>ChessBook</h1>
        <h3>${user.name}</h3>
        <table>
            <thead>
            <tr>
                <td>Name</td>
                <td>Email</td>
                <td>Choice</td>
            </tr>
            </thead>
            <tbody>
            <% List<ChessPlayer> query = cpdm.findByName(request.getParameter("name"));
            <% for (ChessPlayer chessPlayer : query) {%>
            <tr>
                <td><%=chessPlayer.getName()%>
                </td>
                <td><%=chessPlayer.getEmail()%>
                </td>
                <td>
                    <form method="get" action="createGame.jsp">
                        <input type="hidden" name="id" value="<%=chessPlayer.getId()%>">
                        <input type="submit" value="Start">
                    </form>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <p><%=new java.util.Date()%></p>
        <p>
            <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
        </p>
        </body>
        </html>
    </c:when>
    <c:otherwise>
        <html>
        <head>
            <title>ChessBook</title>
        </head>
        <body>
        <h1>ChessBook</h1>
        <h3>${user.name}</h3>
        <h2>No user with name <%=request.getParameter("name")%> were found.</h2>
        <p><a href="profile.jsp">Try again</a></p>
        <p><%=new java.util.Date()%></p>
        <p>
            <a href="logout.jsp">Logout</a> • <a href="profile.jsp">My Games</a>
        </p>
        </body>
        </html>
    </c:otherwise>
</c:choose>