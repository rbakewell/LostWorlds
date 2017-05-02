<%-- 
    Document   : select
    Created on : Apr 20, 2017, 2:08:59 PM
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
        <title>Selection Page</title>
    </head>
    <body>
        <div class="cen">
            <h1>Choose Your Character</h1>
        </div>
        <div class="cen">
            <form action="LWservlet" method="post">
            <input type="image" name="submit" src="skel/skel_cover.jpg" border="0" alt="Submit" value="skel"/>
            <input type="image" name="submit" src="man/man_cover.jpg" border="0" alt="Submit" value="man"/>
            

        </div>
        </form>
            <div class="cen">
            <h1>Hello bottom!</h1>
        </div>
    </body>
</html>
