<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Add Trip</title>
</head>
<body>
<strong>Trip:</strong><br><br>

<form action="addTrip" method="post">
    <strong>departure:   </strong>
    <input list="places" required name="departure" autocomplete="off"><br>
    <strong>destination: </strong>
    <input list="places" required name="destination" autocomplete="off"><br>
    <datalist id="places">
        <c:forEach var="item" items="${places}">
            <option>${item}</option>
        </c:forEach>
    </datalist>
    <strong>date:        </strong>
    <input type="datetime-local" required name="departureDate" value="<c:out value="${departureDate}"/>"/><br>
    <strong>capacity:    </strong>
    <input type="number" required name="capacity" min="1" max="100"><br>
    <strong>price:       </strong>
    <input type="number" required name="price" min="0.01" step=".01"><br>
    <input type="submit" value="add">
</form><br>
<a href="/web/admin/Menu.jsp">Go to menu</a>
</body>
</html>
