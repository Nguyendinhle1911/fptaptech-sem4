<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; text-align: center; }
        .error { color: red; }
        .form-container { width: 300px; margin: 0 auto; }
        input { margin: 10px 0; padding: 5px; width: 100%; }
        button { padding: 10px 20px; }
    </style>
</head>
<body>
<div class="form-container">
    <h2>Login</h2>
    <% if (request.getAttribute("error") != null) { %>
    <p class="error"><%= request.getAttribute("error") %></p>
    <% } %>
    <form action="${pageContext.request.contextPath}/auth" method="post">
        <input type="text" name="username" placeholder="Username" required><br>
        <input type="password" name="password" placeholder="Password" required><br>
        <button type="submit">Login</button>
    </form>
</div>
</body>

</html>