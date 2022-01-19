<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="cpdm" class="persist.ChessPlayerDM" scope="session"/>
<jsp:useBean id="cgdm" class="persist.ChessGameDM" scope="session"/>
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
    <c:choose>
        <c:when test="${cpdm.findByEmail(param.email).get() eq null}">
            <jsp:useBean id="user" class="domain.ChessPlayer" scope="page">
                <jsp:setProperty name="user" property="*"/>
            </jsp:useBean>
            <% cpdm.insert(user); %>
            <h2>User with email "${param.email}" has been registered. </h2>
            <a href="login.jsp" class="ml-3 my-3 btn btn-sm btn-primary btn-block" role="button">Login</a>
        </c:when>
        <c:otherwise>
            <h2>User with email "${param.email}" is already registered. </h2>

            <a href="register.jsp" class="mr-3 my-3 btn btn-sm btn-success btn-block" role="button">Try again</a>
            <span>or</span>
            <a href="login.jsp" class="ml-3 my-3 btn btn-sm btn-primary btn-block" role="button">Login</a>

        </c:otherwise>
    </c:choose>
        <p class="mt-2 mb-3 text-muted"><%= new java.util.Date()%></p>



    </div>
</div>
</body>
</html>