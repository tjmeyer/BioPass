<%-- 
    Document   : loginHome
    Created on : Jun 5, 2015, 6:01:49 AM
    Author     : M
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Home</title>
    </head>
    <body>
        <h1>Hello ${sessionScope.user}</h1>
        <h2>You successfully logged in with the following accuracy:</h2>
        <h3 style="color:green;">Hold Deviation: ${hold}</h3>
        <h3 style='color:green;'>Fly  Deviation: ${fly}</h3>

        <form action="SetDev" method="POST">
            <label>Set Threshold Deviation (min: 1.25, max: 5, default: 2)</label>
            <br/>
            <input type="number" step=".01" min="1.25" max="5" maxlength="3" id="deviation" name="deviation" value="${threshold}"></input>
            <input type="submit" value="Save"></input>
        </form>
        ${message}
        <br/>
        <a href="Logout">Logout</a>
        <hr/>
        <table class="tr1">
            <tr>
                <th>
                    ASCII
                </th>
                <th>
                    Hold Time
                </th>
                <th>
                    Start Time
                </th>
            </tr>
            <c:forEach var="capture" items="${compare.captures}">
                <tr>
                    <td>${capture.ascii}</td>
                    <td>${capture.time}</td>
                    <td>${capture.start}</td>
                </tr>
            </c:forEach>
        </table>
        
        <a href='Delete'>Delete History</a>
        
       
        <!-- Script to capture and send key strokes-->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script type="text/javascript" src="chartRender.js"></script>
        <script type="text/javascript" src="jquery.canvasjs.min.js"></script>
    </body>
</html>
