<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Top 5</title>
</head>
<body>
    <strong>Top 5:</strong><br><br>
    <c:choose>
        <c:when items="${top_5== null}">
            <strong>There is no data enought</strong><br><br>
        </c:when>
        <c:otherwise>
            <c:forEach var="item" items="${top_5}">
                <div>
                    <strong>Nome: </strong>${item.getUsername()}<br>
                    <strong>Email: </strong>${item.getEmail()}<br>
                </div>
            </c:forEach>
        </c:otherwise>
                    </c:choose></div><br><br>

    <a href="/web/secured/Menu.jsp">Go to menu</a>

    <br><br>
    <form action="logout" method="get">
        <input type="submit" value="logout">
    </form>
</body>
</html>


