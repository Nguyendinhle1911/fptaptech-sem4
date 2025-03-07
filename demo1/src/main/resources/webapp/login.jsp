<%--<%@ page contentType="text/html;charset=UTF-8" %>--%>
<%--<html>--%>
<%--<head><title>Login</title></head>--%>
<%--<body>--%>
<%--<h1>Login</h1>--%>
<%--    <form action="<%= request.getContextPath() %>/auth" method="post">--%>
<%--        <p>Username: <input type="text" name="username"/></p>--%>
<%--        <p>Password: <input type="password" name="password"/></p>--%>
<%--        <button type="submit">Login</button>--%>
<%--    </form>--%>
<%--    <%--%>
<%--        String error = (String) request.getAttribute("error");--%>
<%--        if (error != null) {--%>
<%--    %>--%>
<%--    <p style="color:red;"><%= error %></p>--%>
<%--    <%--%>
<%--    }--%>
<%--    %>--%>
<%--</body>--%>
<%--</html>--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h1>Login</h1>
<form action="${pageContext.request.contextPath}/auth" method="post">
    <p>Username: <input type="text" name="username"/></p>
    <p>Password: <input type="password" name="password"/></p>
    <button type="submit">Login</button>
</form>

<!-- Hiển thị thông báo lỗi (nếu có) -->
<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>
</body>
</html>

