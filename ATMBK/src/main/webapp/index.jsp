<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hello World</title>
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
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            padding: 40px;
            text-align: center;
            animation: slideUp 0.6s ease-out;
            max-width: 500px;
        }
        h1 {
            font-weight: 600;
            color: #1a73e8;
            margin-bottom: 30px;
        }
        .btn-primary {
            background: linear-gradient(45deg, #1a73e8, #4285f4);
            border: none;
            padding: 12px 30px;
            font-size: 16px;
            border-radius: 10px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .btn-primary:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
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
<div class="container">
    <h1 class="animate__animated animate__fadeInDown">
        <i class="fas fa-globe me-2"></i>Hello, World!
    </h1>
    <a href="login.jsp" class="btn btn-primary animate__animated animate__fadeIn" style="animation-delay: 0.3s;">
        <i class="fas fa-sign-in-alt me-2"></i>Go to Login
    </a>
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