<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<html>--%>
<%--<head><title>Index</title></head>--%>
<%--<body>--%>
<%--<h1>Welcome to JWT MVC with Annotation</h1>--%>
<%--<p><a href="<%= request.getContextPath() %>/auth">Login Page</a></p>--%>
<%--</body>--%>
<%--</html>--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Index</title>
</head>
<body>
<h1>Welcome to JWT MVC with Annotation</h1>
<!-- Sử dụng EL để lấy contextPath nếu cần -->
<p><a href="${pageContext.request.contextPath}/auth">Login Page</a></p>
</body>
</html>

