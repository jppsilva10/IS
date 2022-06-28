<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Add Place</title>
</head>
<body>
<strong>Add Place</strong><br><br>

<form action="addPlace" method="post">
    <strong>name: </strong>
    <input type="text" required name="name" placeholder="name..."><br>
    <input type="submit" value="add">
</form><br>
<a href="/web/admin/Menu.jsp">Go to menu</a>
</body>
</html>
