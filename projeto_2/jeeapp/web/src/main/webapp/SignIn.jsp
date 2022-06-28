<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sign In</title>
</head>
<body>
<form action="signin" method="post">
    <strong>username:</strong>
    <input required name="username" type="text" placeholder="username..."/><br>
    <strong>email:</strong>
    <input required name="email" type="email" placeholder="email..."/><br>
    <strong>password:</strong>
    <input required name="password" type="password" placeholder="password..."/><br>
    <input type="submit" value="sign in">
</form><br>
<a href="/web/Login.jsp"> Login </a>
</body>
</html>
