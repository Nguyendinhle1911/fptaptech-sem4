<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Withdraw Money</title>
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
        .withdraw-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            padding: 40px;
            width: 100%;
            max-width: 450px;
            animation: slideUp 0.6s ease-out;
            text-align: center;
            transition: transform 0.3s ease-in-out;
        }
        .withdraw-container:hover {
            transform: scale(1.02);
        }
        h2 {
            font-weight: 600;
            color: #1a73e8;
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
        .btn-secondary {
            background: linear-gradient(45deg, #6c757d, #adb5bd);
            border: none;
            width: 100%;
            padding: 12px;
            font-size: 16px;
            border-radius: 10px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .btn-secondary:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }
        .error-message {
            color: #dc3545;
            margin-top: 15px;
            animation: fadeIn 0.5s ease-in;
        }
        /* Style cho toast notification */
        .toast-container {
            position: fixed;
            top: 20px;
            left: 20px;
            z-index: 1055;
        }
        .toast {
            border-radius: 10px;
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
<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp"/>
</c:if>

<!-- Container cho toast notification -->
<div class="toast-container">
    <c:if test="${not empty requestScope.success}">
        <div class="toast align-items-center text-white bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="true" data-bs-delay="3000">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="fas fa-check-circle me-2"></i>${requestScope.success}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
    <c:if test="${not empty requestScope.error}">
        <div class="toast align-items-center text-white bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="true" data-bs-delay="3000">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="fas fa-exclamation-circle me-2"></i>${requestScope.error}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
</div>

<div class="withdraw-container">
    <h2 class="animate__animated animate__fadeInDown">
        <i class="fas fa-money-bill-wave me-2"></i>Withdraw Money
    </h2>
    <form action="${pageContext.request.contextPath}/withdraw" method="post">
        <input type="hidden" name="accountId" value="${param.accountId}">
        <div class="mb-3 animate__animated animate__fadeIn" style="animation-delay: 0.2s;">
            <div class="input-group">
                <span class="input-group-text"><i class="fas fa-coins"></i></span>
                <input type="number" name="amount" class="form-control" step="0.01" min="0.01"
                       placeholder="Enter the amount you want to withdraw" required oninput="validateAmount(this)">
            </div>
        </div>
        <button type="submit" class="btn btn-primary animate__animated animate__fadeIn" style="animation-delay: 0.3s;">
            <i class="fas fa-arrow-up me-2"></i>Withdraw
        </button>
    </form>
    <a href="${pageContext.request.contextPath}/dashboard.jsp" class="btn btn-secondary mt-3 animate__animated animate__fadeIn" style="animation-delay: 0.4s;">
        <i class="fas fa-arrow-left me-2"></i>Back to Dashboard
    </a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.js"></script>
<script>
    function validateAmount(input) {
        if (input.value < 0) {
            input.value = ""; // Xóa giá trị nếu nhập số âm
        }
    }

    document.addEventListener("DOMContentLoaded", function() {
        const amountInput = document.querySelector('input[name="amount"]');
        amountInput.addEventListener("keydown", function(event) {
            if (event.key === "-" || event.key === "e") {
                event.preventDefault(); // Ngăn không cho nhập dấu '-' và 'e'
            }
        });

        // Hiển thị toast nếu có
        const toasts = document.querySelectorAll('.toast');
        toasts.forEach(toast => {
            new bootstrap.Toast(toast).show();
        });
    });

    document.querySelectorAll('.btn').forEach(btn => {
        btn.addEventListener('click', function() {
            this.classList.add('animate__animated', 'animate__pulse');
            setTimeout(() => this.classList.remove('animate__animated', 'animate__pulse'), 500);
        });
    });
</script>
</body>
</html>