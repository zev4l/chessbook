<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
<html>
<head>
    <title>ChessBook</title>
    <link rel="stylesheet" type="text/css" href="styles/styles.css">
</head>
<body>
    <h1>ChessBook</h1>
    <c:choose>
        <c:when test="${cpdm.findByEmail(param.email).get() eq null}">
            <jsp:useBean id="user" class="domain.ChessPlayer" scope="page">
                <jsp:setProperty name="user" property="*"/>
            </jsp:useBean>
            <% cpdm.insert(user); %>
            <h2>User with email ${param.email} has been registered. </h2>
            <p>
                <a href="login.jsp">Login</a>
            </p>
        </c:when>
        <c:otherwise>
            <h2>User with email ${param.email} is already registered. </h2>
            <p>
                <a href="register.jsp">Try again </a> or <a href="login.jsp">Login</a>
            </p>
        </c:otherwise>
    </c:choose>
<%= new java.util.Date() %>
</body>
</html>