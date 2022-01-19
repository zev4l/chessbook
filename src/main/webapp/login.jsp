<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>ChessBook</title>
        <link rel="stylesheet" type="text/css" href="styles/styles.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
        <link rel="icon" href="styles/icon.png">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">


    </head>
    <body class="text-center vh-100">

        <div class="d-flex justify-content-center align-items-center">
            <div class="container">
                <h1 class="h1 mb-3 font-weight-normal">ChessBook</h1>
                <img class="my-3 logo" src="styles/icon.png" alt="" width="72" height="72">
                <h2 class="h2 my-3 font-weight-normal">Login</h2>
                <form method="POST" action="processLogin.jsp" class="form-container">
                    <div class="mb-3">
                        <label for="inputName" class="form-label">Name</label>
                        <input type="text" name="name" id="inputName" class="form-control" placeholder="Username" >
                    </div>
                    <div class="mb-3">
                        <label for="inputEmail" class="form-label">Email</label>
                        <input type="text" name="email" id="inputEmail" class="form-control" placeholder="Email address">
                    </div>
                    <button class="btn btn-lg btn-primary btn-block mt-2" type="submit">Sign in</button>
                    <p class="mt-3 mb-3">or</p>
                    <p><a class="btn btn-sm btn-outline-secondary btn-block" href="register.jsp"> Register </a></p>
                </form>

                <p class="mt-2 mb-3 text-muted"><%= new java.util.Date()%></p>
            </div>
        </div>
    </body>
</html>
