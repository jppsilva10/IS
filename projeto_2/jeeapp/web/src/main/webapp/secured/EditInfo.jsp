<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>EditMyInformation</title>
</head>
<body>
    <h1>Edit your information</h1>
    <form action="editInfo" method="post">
        <strong>new email: </strong>
        <input required name="New_email" type="text" placeholder="email..."/><br>
        <strong>new username: </strong>
        <input required name="New_username" type="text" placeholder="username..."/><br>
        <strong>new password: </strong>
        <input required name="New_password" type="password" placeholder="password..."/><br>
        <input type="submit" value="edit">
    </form>
    <button onclick="window.location.href = '/web/secured/Menu.jsp'">Back</button>

    <br><br>
    <form action="logout" method="get">
        <input type="submit" value="logout">
    </form>
</body>
</html>
