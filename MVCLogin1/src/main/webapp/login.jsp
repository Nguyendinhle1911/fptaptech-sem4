<%--
  Created by IntelliJ IDEA.
  User: nguyen
  Date: 13/02/2025
  Time: 11:40 SA
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(to right, #00c6ff, #0072ff);
            font-family: 'Poppins', sans-serif;
            color: white;
        }
        .login-container {
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 30px;
            width: 100%;
            max-width: 400px;
            box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.2);
        }
        .form-control {
            background: rgba(255, 255, 255, 0.3);
            border: none;
            color: white;
        }
        .form-control:focus {
            background: rgba(255, 255, 255, 0.5);
            box-shadow: none;
            border: none;
        }
        .form-label {
            font-weight: 600;
        }
        .btn-primary {
            background: #ff6b6b;
            border: none;
            transition: all 0.3s ease-in-out;
        }
        .btn-primary:hover {
            background: #ff3d3d;
        }
        .text-danger {
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="login-container text-center">
        <h2 class="mb-4"><i class="fas fa-user-circle"></i> Đăng nhập</h2>
        <form action="login" method="post">
            <div class="mb-3 text-start">
                <label for="username" class="form-label">Tên đăng nhập</label>
                <input type="text" id="username" name="username" class="form-control" required>
            </div>
            <div class="mb-3 text-start">
                <label for="password" class="form-label">Mật khẩu</label>
                <input type="password" id="password" name="password" class="form-control" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">
                <i class="fas fa-sign-in-alt"></i> Đăng nhập
            </button>
        </form>
        <p class="text-danger mt-3">${error}</p>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
