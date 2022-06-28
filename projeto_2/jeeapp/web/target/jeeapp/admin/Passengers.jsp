<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Passengers</title>
</head>
<body>
<strong>Passengers:</strong><br><br>

<c:forEach var="item" items="${users}">
    <div><strong>Id: </strong>${item.getId()}<br>
        <strong>Username: </strong>${item.getUsername()}<br>
        <strong>Email: </strong>${item.getEmail()}
        </div><br><br>
</c:forEach>
<a href="/web/admin/Menu.jsp">Go to menu</a>

</body>
</html>
