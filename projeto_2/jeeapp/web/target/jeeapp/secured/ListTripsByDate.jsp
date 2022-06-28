<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Trips</title>
</head>
<body>
<strong>Trips:</strong><br><br>

<form action="listTripsByDate" method="get">
    <input type="datetime-local" name="start" value="<c:out value="${start}"/>"/>
    <input type="datetime-local" name="end" value="<c:out value="${end}"/>"/>
    <input type="submit" value="update">
</form><br><br>

<c:forEach var="item" items="${myList}">
    <div><strong>From: </strong>${item.getDeparture()} <strong>To: </strong>${item.getDestination()}<br>
        <strong>Date: </strong>${item.getDepartureDate()}<br>
        <strong>Price: </strong>${item.getPrice()}€<br>
        <c:choose>
            <c:when test="${item.getCapacity()>item.getTickets()}">
                <form action="listPlaces" method="get">
                    <input hidden name="trip" value="<c:out value="${item.getId()}"/>"/>
                    <input type="submit" value="BUY">
                </form>
            </c:when>
            <c:otherwise></c:otherwise>
        </c:choose></div><br><br>
    </c:forEach>
<a href="/web/secured/Menu.jsp">Go to menu</a>

<br><br>
<form action="logout" method="get">
    <input type="submit" value="logout">
</form>
</body>
</html>