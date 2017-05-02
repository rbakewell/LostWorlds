<%-- 
    Document   : login
    Created on : Apr 20, 2017, 11:19:49 AM
    Author     : russ
--%>
<link rel = "stylesheet"
      type = "text/css"
      href = "StyleSheet.css" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lost worlds Log in</title>
    </head>
    <body>
        <div class="cen">
            <h1>Welcome to Lost Worlds!</h1>
            <h3>Fantasy Combat Book Game</h3>
            <form action="LWservlet" method="post">
                <input type="submit" name="login" value="Log in"/>
                <input type="text" name="user"/>
            </form>
        </div>
    </body>
</html>
