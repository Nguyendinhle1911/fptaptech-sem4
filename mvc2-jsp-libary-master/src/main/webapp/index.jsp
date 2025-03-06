<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            text-align: center;
        }
        .welcome {
            color: #333;
            padding: 20px;
        }
    </style>
</head>
<body>
<div class="welcome">
    <h1>Chào mừng đến với trang web của tôi!</h1>
    <p>Hôm nay là: <%= new java.util.Date() %></p>
    <%
        String user = (String) session.getAttribute("username");
        if (user == null) {
            user = "Khách";
        }
    %>
    <p>Xin chào, <%= user %>!</p>
</div>
</body>
</html>