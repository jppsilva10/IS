<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Return Ticket Info</title>
</head>
<body>
<c:choose>
    <c:when test="${out}">
        <strong>Ticket returned</strong><br><br>
    </c:when>
    <c:otherwise>
        <strong>You cant return the ticket</strong><br><br>
    </c:otherwise>
</c:choose></div><br><br>


<a href="/web/secured/ListUserTrips.jsp">See my trips</a><br><br>
<a href="/web/secured/Menu.jsp">Go to menu</a>

<br><br>
<form action="logout" method="get">
    <input type="submit" value="logout">
</form>
</body>
</html>