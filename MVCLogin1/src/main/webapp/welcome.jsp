<%@ page import="util.JwtUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.Cookie, util.JwtUtil" %>
<%
    String username = null;
    String token = null;

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("jwt_token".equals(cookie.getName())) {
                token = cookie.getValue();
                username = JwtUtil.validateToken(token);
            }
        }
    }

    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Welcome</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(to right, #234048, #234048);
            color: white;
            font-family: 'Poppins', sans-serif;
        }
        .navbar {
            background-color: rgba(0, 0, 0, 0.8) !important;
            padding: 15px;
        }
        .welcome-card {
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(10px);
            border-radius: 12px;
            padding: 40px;
            box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
            text-align: center;
        }
        .welcome-card h2 {
            font-weight: bold;
        }
        .btn-custom {
            font-size: 18px;
            padding: 12px 24px;
            border-radius: 8px;
            transition: all 0.3s ease-in-out;
        }
        .btn-primary:hover {
            background: #ff6b6b;
            border-color: #ff6b6b;
        }
        .btn-secondary:hover {
            background: #ffcc00;
            border-color: #ffcc00;
            color: #333;
        }
        .btn-danger {
            transition: all 0.3s ease-in-out;
        }
        .btn-danger:hover {
            background: #ff3333;
            border-color: #ff3333;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="#">
            <i class="fas fa-user-circle"></i> Xin chào, <%= username %>
        </a>
        <form method="get" action="logout">
            <button type="submit" class="btn btn-danger btn-custom" onclick="return confirm('Bạn có chắc muốn đăng xuất?');">
                <i class="fas fa-sign-out-alt"></i> Đăng xuất
            </button>
        </form>
    </div>
</nav>

<div class="container d-flex justify-content-center align-items-center" style="height: 80vh;">
    <div class="welcome-card">
        <h2>Chào mừng, <%= username %>! 🎉</h2>
        <p class="text-light">Khám phá các sản phẩm tuyệt vời của chúng tôi</p>
        <div class="mt-4">
            <a href="product.jsp" class="btn btn-primary btn-custom">
                <i class="fas fa-box-open"></i> Sản phẩm
            </a>
            <a href="productjstl.jsp" class="btn btn-secondary btn-custom">
                <i class="fas fa-list"></i> Sản phẩm JSTL
            </a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
