<%-- 
    Document   : loginHome
    Created on : Jun 5, 2015, 6:01:49 AM
    Author     : M
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Home</title>
    </head>
    <body>
        <h1>Hello ${sessionScope.user}</h1>
        <a href="Logout">Logout</a>
        <form action="SetData">
            <label>Set Threshold Deviation (min: 1.25, max: 5, default: 2)</label>
            <br/>
            <input type="text" id ="deviation" placeholder="${sessionScope.deviation}"></input>
            <input type="submit" value="Save"></input>
        </form>
        <hr/>
        <div id="chartContainer"></div>
        <hr/>
        
       
        <!-- Script to capture and send key strokes-->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script type="text/javascript" src="chartRender.js"></script>
        <script type="text/javascript" src="jquery.canvasjs.min.js"></script>
    </body>
</html>
