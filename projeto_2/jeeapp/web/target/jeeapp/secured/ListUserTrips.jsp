<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Trips</title>
</head>
<body>
<strong>Your Trips:</strong><br><br>
<c:forEach var="item" items="${trips}">
    <div>
        <strong>Ticket: </strong>${item.getId()}<br>
        <strong>From: </strong>${item.getTrip_id().getDeparture()} <strong>To: </strong>${item.getTrip_id().getDestination()}<br>
        <strong>Date: </strong>${item.getTrip_id().getDepartureDate()}<br>
        <strong>Price: </strong>${item.getTrip_id().getPrice()}€<br>
        <strong>Place: </strong>${item.getPlace()}<br>
        <c:choose>
            <c:when test="${item.getTrip_id().getDepartureDate().after(now)}">
                <form action="returnTicket" method="post">
                    <input hidden name="ticket" value="<c:out value="${item.getId()}"/>"/>
                    <input type="submit" value="RETURN">
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
