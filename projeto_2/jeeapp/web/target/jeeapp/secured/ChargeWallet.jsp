<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Trips</title>
</head>
<body>
<strong>Account Balance:</strong> <c:out value="${wallet}"/>€

<form action="chargeWallet" method="post">
    <input type="number" required name="amount" min="0.01" step=".01"><br>
    <input type="submit" value="charge">
</form>
<a href="/web/secured/Menu.jsp">Go to menu</a>

<br><br>
<form action="logout" method="get">
    <input type="submit" value="logout">
</form>
</body>
</html>

