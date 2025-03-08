<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>🏠 Home</title>

    <!-- ✅ Add Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- ✅ Add FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #007bff, #00bfff);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            text-align: center;
            color: white;
        }
        .container {
            background: rgba(255, 255, 255, 0.9);
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0px 0px 25px rgba(0, 0, 0, 0.3);
            max-width: 600px;
            width: 100%;
        }
        h1 {
            color: #007bff;
            margin-bottom: 20px;
            font-size: 2.5rem;
            font-weight: bold;
        }
        p {
            color: #495057;
            font-size: 1.2rem;
            margin-bottom: 30px;
        }
        .btn-primary {
            font-size: 18px;
            padding: 12px 25px;
            border-radius: 50px;
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
        .logo {
            width: 80px;
            height: 80px;
            margin-bottom: 20px;
            animation: float 3s ease-in-out infinite;
        }
        @keyframes float {
            0%, 100% {
                transform: translateY(0);
            }
            50% {
                transform: translateY(-10px);
            }
        }
    </style>
</head>
<body>

<div class="container">
    <img src="https://cdn-icons-png.flaticon.com/512/1995/1995485.png" alt="Logo" class="logo">
    <h1>🚀 Welcome to JWT MVC with Annotation</h1>
    <p>Login system with JSON Web Token</p>
    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary">
        <i class="fas fa-sign-in-alt"></i> Go to Login Page
    </a>
</div>

<!-- ✅ Add Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>