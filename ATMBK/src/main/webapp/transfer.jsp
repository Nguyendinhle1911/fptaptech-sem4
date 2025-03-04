<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Transfer Money</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Font Awesome CDN -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="bg-gradient-to-br from-blue-100 via-indigo-200 to-purple-200 min-h-screen font-sans flex items-center justify-center p-4">
<c:if test="${empty sessionScope.user}">
    <c:redirect url="login.jsp"/>
</c:if>

<!-- Toast Notification -->
<div class="fixed top-5 left-5 z-50">
    <c:if test="${not empty requestScope.success}">
        <div class="bg-green-500 text-white p-4 rounded-xl shadow-lg flex items-center space-x-2 animate-fade-in-up opacity-0 transition-opacity duration-500" id="success-toast">
            <i class="fas fa-check-circle"></i>
            <span>${requestScope.success}</span>
            <button type="button" class="ml-2 text-white hover:text-gray-200" onclick="this.parentElement.style.opacity='0'; setTimeout(() => this.parentElement.remove(), 500);">
                <i class="fas fa-times"></i>
            </button>
        </div>
    </c:if>
    <c:if test="${not empty requestScope.error}">
        <div class="bg-red-500 text-white p-4 rounded-xl shadow-lg flex items-center space-x-2 animate-fade-in-up opacity-0 transition-opacity duration-500" id="error-toast">
            <i class="fas fa-exclamation-circle"></i>
            <span>${requestScope.error}</span>
            <button type="button" class="ml-2 text-white hover:text-gray-200" onclick="this.parentElement.style.opacity='0'; setTimeout(() => this.parentElement.remove(), 500);">
                <i class="fas fa-times"></i>
            </button>
        </div>
    </c:if>
</div>

<!-- Transfer Container -->
<div class="bg-white bg-opacity-90 backdrop-blur-md rounded-3xl shadow-2xl p-8 w-full max-w-md transform transition-all hover:scale-105 animate-slide-up">
    <h2 class="text-3xl font-bold text-indigo-600 flex items-center justify-center mb-8 animate-fade-in-down">
        <i class="fas fa-exchange-alt mr-3 text-indigo-500 animate-pulse"></i>Transfer Money
    </h2>
    <form action="${pageContext.request.contextPath}/transfer" method="post">
        <input type="hidden" name="fromAccountId" value="${param.accountId}">
        <div class="mb-6 animate-fade-in" style="animation-delay: 0.2s;">
            <label class="block text-gray-700 font-medium mb-2 flex items-center">
                <i class="fas fa-university mr-2 text-indigo-400"></i>Target Account Number
            </label>
            <div class="relative">
                <span class="absolute left-3 top-1/2 transform -translate-y-1/2 text-indigo-400">
                    <i class="fas fa-id-card"></i>
                </span>
                <input type="text" name="toAccountNumber"
                       class="w-full pl-10 p-4 rounded-xl border border-indigo-300 text-gray-700 placeholder-gray-400 focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/30 transition-all duration-300 hover:bg-indigo-50"
                       placeholder="Enter transfer account" required>
            </div>
        </div>
        <div class="mb-6 animate-fade-in" style="animation-delay: 0.3s;">
            <label class="block text-gray-700 font-medium mb-2 flex items-center">
                <i class="fas fa-dollar-sign mr-2 text-indigo-400"></i>Amount
            </label>
            <div class="relative">
                <span class="absolute left-3 top-1/2 transform -translate-y-1/2 text-indigo-400">
                    <i class="fas fa-money-bill-wave"></i>
                </span>
                <input type="number" name="amount" step="0.01" min="0.01" max="5000"
                       class="w-full pl-10 p-4 rounded-xl border border-indigo-300 text-gray-700 placeholder-gray-400 focus:border-indigo-500 focus:ring-4 focus:ring-indigo-500/30 transition-all duration-300 hover:bg-indigo-50"
                       placeholder="Enter amount to transfer" required oninput="validateAmount(this)">
            </div>
        </div>
        <button type="submit"
                class="w-full bg-gradient-to-r from-indigo-600 to-blue-500 text-white py-3 rounded-xl font-semibold flex items-center justify-center hover:from-indigo-700 hover:to-blue-600 hover:shadow-lg transform hover:-translate-y-1 transition-all duration-300 animate-fade-in"
                style="animation-delay: 0.4s;">
            <i class="fas fa-paper-plane mr-2"></i>Transfer
        </button>
    </form>
    <a href="${pageContext.request.contextPath}/dashboard.jsp"
       class="mt-4 block w-full bg-gradient-to-r from-gray-500 to-gray-600 text-white py-3 rounded-xl font-semibold flex items-center justify-center hover:from-gray-600 hover:to-gray-700 hover:shadow-lg transform hover:-translate-y-1 transition-all duration-300 animate-fade-in"
       style="animation-delay: 0.5s;">
        <i class="fas fa-arrow-left mr-2"></i>Back to Dashboard
    </a>
</div>

<script>
    // Validate amount input
    function validateAmount(input) {
        if (input.value < 0 || input.value > 5000) input.value = "";
    }

    document.addEventListener("DOMContentLoaded", function() {
        const amountInput = document.querySelector('input[name="amount"]');
        amountInput.addEventListener("keydown", function(event) {
            if (event.key === "-" || event.key === "e") event.preventDefault();
        });

        // Show toasts with animation
        const toasts = document.querySelectorAll('#success-toast, #error-toast');
        toasts.forEach(toast => {
            toast.style.opacity = '1';
            setTimeout(() => {
                toast.style.opacity = '0';
                setTimeout(() => toast.remove(), 500);
            }, 3000);
        });

        // Button click animation
        document.querySelectorAll('button, a').forEach(btn => {
            btn.addEventListener('mousedown', function() {
                this.style.transform = 'scale(0.95)';
            });
            btn.addEventListener('mouseup', function() {
                this.style.transform = 'scale(1.02)';
                setTimeout(() => this.style.transform = 'scale(1)', 150);
            });
        });
    });
</script>
</body>
</html>