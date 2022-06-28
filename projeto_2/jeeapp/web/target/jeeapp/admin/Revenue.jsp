<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Revenue</title>
</head>
<body>
<strong>Revenue:</strong> <c:out value="${revenue}"/>€<br><br>

<form action="revenue" method="get">
    <input type="date" name="date" value="<c:out value="${date}"/>"/>
    <input type="submit" value="update">
</form><br><br>

<a href="/web/admin/Menu.jsp">Go to menu</a>

<br><br>
</body>
</html>
