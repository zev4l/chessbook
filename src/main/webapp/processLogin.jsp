<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="page"/>
<%--<%@ page import="persist.ChessPlayerDM"%>--%>

<c:choose>
    <c:when test="${cpdm.findByEmail(param.email).get() eq null}">
        <html>
            <head>
                <title>ChessBook</title>
            </head>
            <body>
            <h1>ChessBook</h1>
            <h2>Login Failed !</h2>
            <h3>The user ${param.email} was not found.</h3>
            <p>
            You may:<br>
            <a href="login.jsp">Try Again</a> or <a href="register.jsp">Register</a>
            </p>
            <%= new java.util.Date() %>
            </body>
        </html>
    </c:when>
    <c:otherwise>
        <jsp:useBean id="user" class="domain.ChessPlayer" scope="session">
            <jsp:setProperty name="user" property="*"/>
        </jsp:useBean>
        <jsp:forward page="profile.jsp"/>
    </c:otherwise>
</c:choose>