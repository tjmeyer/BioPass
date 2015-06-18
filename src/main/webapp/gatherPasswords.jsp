<%-- 
    Document   : gatherPasswords
    Created on : May 20, 2015, 4:11:27 AM
    Author     : M
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Password Learning</title>
    </head>
    <body>
        <h1>Learning ${user}'s Biometric Profile</h1>
        <hr/>
        <form method="POST" action="SavePassword">
            <label>Password attempt: ${attemptNumber}</label><br/>
            <input type="password" placeholder="password" name="password" id="passwordInput" onkeydown="startCapture(event);" onkeyup="endCapture(event);"/>
            <input type="reset" onclick="reset();" id="reset" name="reset"/>
            <input type="submit" value="Submit"/>
            <input type="hidden" value="test" id="capture" name="capture"/>
        </form>
            <hr/>
            <div id="messages">
                
            </div>
        <!-- Script to capture and send key strokes-->
        <script type="text/javascript" src="PassCapture.js"></script>
    </body>
</html>
