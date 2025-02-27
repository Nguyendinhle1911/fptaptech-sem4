<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>ATM Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #e0eafc, #cfdef3);
            min-height: 100vh;
            font-family: 'Poppins', sans-serif;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            padding: 40px;
            width: 100%;
            max-width: 400px;
            animation: slideUp 0.6s ease-out;
        }
        h2 {
            font-weight: 600;
            color: #1a73e8;
            text-align: center;
            margin-bottom: 30px;
        }
        .form-label {
            font-weight: 500;
            color: #333;
        }
        .form-control {
            border-radius: 10px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        .form-control:focus {
            border-color: #1a73e8;
            box-shadow: 0 0 10px rgba(26, 115, 232, 0.2);
        }
        .btn-primary {
            background: linear-gradient(45deg, #1a73e8, #4285f4);
            border: none;
            width: 100%;
            padding: 12px;
            font-size: 16px;
            border-radius: 10px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .btn-primary:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }
        .error-message {
            color: #dc3545;
            text-align: center;
            margin-top: 15px;
            animation: fadeIn 0.5s ease-in;
        }
        @keyframes slideUp {
            from { transform: translateY(50px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2 class="animate__animated animate__fadeInDown">
        <i class="fas fa-lock me-2"></i>ATM Login
    </h2>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="mb-3 animate__animated animate__fadeIn" style="animation-delay: 0.2s;">
            <label class="form-label">Username</label>
            <div class="input-group">
                <span class="input-group-text"><i class="fas fa-user"></i></span>
                <input type="text" name="username" class="form-control" required>
            </div>
        </div>
        <div class="mb-3 animate__animated animate__fadeIn" style="animation-delay: 0.3s;">
            <label class="form-label">Password</label>
            <div class="input-group">
                <span class="input-group-text"><i class="fas fa-key"></i></span>
                <input type="password" name="password" class="form-control" required>
            </div>
        </div>
        <button type="submit" class="btn btn-primary animate__animated animate__fadeIn" style="animation-delay: 0.4s;">
            <i class="fas fa-sign-in-alt me-2"></i>Login
        </button>
    </form>
    <c:if test="${not empty requestScope.error}">
        <p class="error-message animate__animated animate__shakeX">${requestScope.error}</p>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.js"></script>
<script>
    document.querySelector('.btn-primary').addEventListener('click', function() {
        this.classList.add('animate__animated', 'animate__pulse');
        setTimeout(() => this.classList.remove('animate__animated', 'animate__pulse'), 500);
    });
</script>
</body>
</html>