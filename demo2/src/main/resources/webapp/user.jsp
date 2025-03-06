<%--<%@ page contentType="text/html;charset=UTF-8" %>--%>
<%--<%--%>
<%--    String username = (String) request.getAttribute("username");--%>
<%--    String role = (String) request.getAttribute("role");--%>
<%--%>--%>
<%--<html>--%>
<%--<head><title>User Page</title></head>--%>
<%--<body>--%>
<%--    <h1>User Page</h1>--%>
<%--    <p>Hello <b><%= username %></b> (role = <%= role %>)</p>--%>
<%--    <a href="<%= request.getContextPath() %>/webapp">Home</a>--%>
<%--</body>--%>
<%--</html>--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Page</title>
</head>
<body>
<h1>User Page</h1>
<!-- 'username' và 'role' được set trong Filter/Servlet -->
<p>Xin chào <b>${username}</b>, role: ${role}</p>

<p><a href="${pageContext.request.contextPath}/">Trang chủ</a></p>
</body>
</html>

