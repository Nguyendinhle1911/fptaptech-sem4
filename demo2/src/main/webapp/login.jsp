<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>🔐 Login</title>

    <!-- ✅ Add Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- ✅ Add FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        body {
            font-family: 'Arial', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }
        .login-container {
            background: rgba(255, 255, 255, 0.9);
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0px 0px 25px rgba(0, 0, 0, 0.3);
            width: 400px;
            text-align: center;
            animation: fadeIn 1s ease-in-out;
        }
        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(-20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        h2 {
            color: #007bff;
            margin-bottom: 25px;
            font-size: 2rem;
            font-weight: bold;
        }
        .form-control {
            margin-bottom: 20px;
            border-radius: 5px;
            border: 1px solid #ced4da;
            padding: 12px;
            font-size: 16px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        .form-control:focus {
            border-color: #80bdff;
            outline: 0;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
        }
        .btn-primary {
            width: 100%;
            font-size: 18px;
            padding: 12px;
            border-radius: 5px;
            background-color: #007bff;
            border: none;
            transition: background-color 0.3s ease, transform 0.3s ease;
        }
        .btn-primary:hover {
            background-color: #0056b3;
            transform: translateY(-3px);
        }
        .btn-primary i {
            margin-right: 10px;
        }
        .error-message {
            color: #dc3545;
            margin-top: 15px;
            font-size: 14px;
            font-weight: bold;
        }
        .forgot-password {
            margin-top: 15px;
            font-size: 14px;
            color: #495057;
        }
        .forgot-password a {
            color: #007bff;
            text-decoration: none;
            transition: color 0.3s ease;
        }
        .forgot-password a:hover {
            color: #0056b3;
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="login-container">
    <h2><i class="fas fa-user-lock"></i> Login</h2>

    <form action="${pageContext.request.contextPath}/auth" method="post">
        <div class="mb-3">
            <input type="text" name="username" class="form-control" placeholder="Username" required>
        </div>

        <div class="mb-3">
            <input type="password" name="password" class="form-control" placeholder="Password" required>
        </div>

        <button type="submit" class="btn btn-primary"><i class="fas fa-sign-in-alt"></i> Login</button>
    </form>

    <!-- Display error message (if any) -->
    <c:if test="${not empty error}">
        <p class="error-message">${error}</p>
    </c:if>

    <!-- Forgot password link -->
    <div class="forgot-password">
        <a href="#">Forgot password?</a>
    </div>
</div>

<!-- ✅ Add Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>