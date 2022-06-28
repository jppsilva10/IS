<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Delete Trip Info</title>
</head>
<body>
<c:choose>
    <c:when test="${out==-1}">
        <strong>The trip is no longer available</strong><br><br>
    </c:when>
    <c:when test="${out==-2}">
        <strong>You dont have enough money</strong><br><br>
    </c:when>
    <c:when test="${out==-3}">
        <strong>The place selected is no longer available</strong><br><br>
    </c:when>
    <c:otherwise>
        <strong>Trip deleted</strong><br><br>
    </c:otherwise>
</c:choose></div><br><br>


<a href="/web/admin/Menu.jsp">Go to menu</a>

<br><br>
</body>
</html>