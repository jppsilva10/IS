<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Log In</title>
</head>
<body>
    <form action="login" method="post">
        <strong>username: </strong>
        <input required name="username" type="text" placeholder="username..."/><br>
        <strong>password: </strong>
        <input required name="password" type="password" placeholder="password..."/><br>
        <input type="submit" value="login">
    </form>
</body>
</html>
