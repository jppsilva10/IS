
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Menu</title>
</head>
<body>
    <a href="/web/secured/ListTripsByDate.jsp">List trips</a> <br><br>
    <a href="/web/secured/ListUserTrips.jsp">List my trips</a> <br><br>
    <a href="/web/secured/ChargeWallet.jsp">Charge my wallet</a> <br><br>
    <a href="/web/secured/EditInfo.jsp">Edit my information</a> <br><br>
    <a href="/web/secured/delete">Delete account</a> <br><br>

    <br><br>
    <form action="logout" method="get">
        <input type="submit" value="logout">
    </form>
</body>
</html>
