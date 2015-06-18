<%-- 
    Document   : createAccount
    Created on : May 20, 2015, 3:45:27 AM
    Author     : M
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create New Account</title>
    </head>
    <body>
        <h1>Create New Account</h1>
        <hr/>
        <form action="NewAccount" method="POST" style="display: ${usernameForm}">
            <label>username</label>
            <input type="text" placeholder="username" name="username" value="${username}"/>
            <input type="submit" name="submit" value="submit"/>
        </form>
        <hr/>
        <div id="messages">
            ${messages}
        </div>
    </body>
</html>
