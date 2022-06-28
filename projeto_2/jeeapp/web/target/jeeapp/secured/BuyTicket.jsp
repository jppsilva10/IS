<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Buy Ticket</title>
</head>
<body>
<strong>Place:</strong>

<form action="buyTicket" method="post">
    <input type="hidden" name="trip" value="<c:out value="${trip}"/>"/>
    <input list="places" required name="place" autocomplete="off"><br>
    <datalist id="places">
        <c:forEach var="item" items="${places}">
            <option>${item.intValue()}</option>
        </c:forEach>
    </datalist>
    <input type="submit" value="BUY">
</form>
</body>
</html>
