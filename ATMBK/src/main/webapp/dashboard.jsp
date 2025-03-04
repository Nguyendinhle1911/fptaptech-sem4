<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <!-- Tailwind CSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>
    <!-- Font Awesome CDN -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        /* Custom animations */
        @keyframes slideUp {
            from { transform: translateY(50px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }
        @keyframes fadeInScale {
            from { opacity: 0; transform: scale(0.95); }
            to { opacity: 1; transform: scale(1); }
        }
        @keyframes pulseGlow {
            0% { box-shadow: 0 0 5px rgba(79, 70, 229, 0.5); }
            50% { box-shadow: 0 0 20px rgba(79, 70, 229, 0.8); }
            100% { box-shadow: 0 0 5px rgba(79, 70, 229, 0.5); }
        }
        .animate-slide-up {
            animation: slideUp 0.8s ease-out forwards;
        }
        .animate-fade-in-scale {
            animation: fadeInScale 0.6s ease-in-out forwards;
        }
        .animate-pulse-glow {
            animation: pulseGlow 2s infinite ease-in-out;
        }
        .hover-lift {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .hover-lift:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
        }
        .hover-glow {
            transition: box-shadow 0.3s ease;
        }
        .hover-glow:hover {
            box-shadow: 0 0 15px rgba(79, 70, 229, 0.7);
        }
    </style>
</head>
<body class="bg-gradient-to-br from-indigo-900 via-purple-900 to-pink-900 min-h-screen font-sans text-gray-200">
<div class="container mx-auto mt-12 px-6">
    <c:if test="${empty sessionScope.user}">
        <c:redirect url="login.jsp"/>
    </c:if>
    <!-- Header -->
    <div class="bg-white bg-opacity-10 backdrop-blur-lg rounded-3xl shadow-2xl p-8 mb-10 animate-slide-up border border-indigo-500/20 relative">
        <h2 class="text-4xl font-extrabold text-white flex items-center justify-between tracking-tight">
            <div class="flex items-center">
                <i class="fas fa-user-circle mr-4 text-indigo-400 animate-pulse"></i>Welcome, <span class="text-indigo-300 ml-2">${sessionScope.user.username}</span>
            </div>
            <i class="fas fa-check-circle text-green-400 text-5xl" title="Login successful"></i>
        </h2>
        <!-- Chỉ thay đổi phần form logout trong Header -->
        <p class="text-indigo-200 mt-3 flex items-center text-lg">
            <i class="fas fa-clock mr-3 text-indigo-400"></i>Token expires at:
            <span class="font-medium text-white ml-2 bg-indigo-700/30 px-3 py-1 rounded-full">
            <fmt:formatDate value="${sessionScope.tokenExpiration}" pattern="dd/MM/yyyy HH:mm:ss"/>
        </span>
        </p>
        <form action="${pageContext.request.contextPath}/logout" method="post" class="inline">
            <button type="submit" class="bg-gradient-to-r from-red-500 to-red-600 text-white px-4 py-2 rounded-xl hover-lift hover-glow flex items-center">
                <i class="fas fa-sign-out-alt mr-2"></i>Logout
            </button>
        </form>
    </div>

    <!-- Accounts Table -->
    <div class="bg-white bg-opacity-10 backdrop-blur-lg rounded-3xl shadow-2xl p-8 mb-10 animate-fade-in-scale border border-indigo-500/20">
        <h3 class="text-3xl font-semibold text-white mb-6 tracking-wide">Your Accounts</h3>
        <div class="overflow-x-auto">
            <table class="w-full text-left border-separate border-spacing-0">
                <thead class="bg-indigo-800 text-white">
                <tr>
                    <th class="p-5 rounded-tl-2xl font-bold text-lg">Account Number</th>
                    <th class="p-5 font-bold text-lg">Balance</th>
                    <th class="p-5 rounded-tr-2xl font-bold text-lg">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="account" items="${sessionScope.accounts}" varStatus="loop">
                    <tr class="hover:bg-indigo-700/20 transition-colors duration-300 animate-fade-in-scale" style="animation-delay: ${loop.index * 0.1}s;">
                        <td class="p-5 border-b border-indigo-500/20 text-white">${account.accountNumber}</td>
                        <td class="p-5 border-b border-indigo-500/20 font-bold text-green-400">$${account.balance}</td>
                        <td class="p-5 border-b border-indigo-500/20 flex gap-3">
                            <a href="${pageContext.request.contextPath}/withdraw.jsp?accountId=${account.accountId}"
                               class="bg-gradient-to-r from-yellow-500 to-yellow-600 text-white px-4 py-2 rounded-xl hover-lift hover-glow flex items-center">
                                <i class="fas fa-money-bill-wave mr-2"></i>Withdraw
                            </a>
                            <a href="${pageContext.request.contextPath}/transfer.jsp?accountId=${account.accountId}"
                               class="bg-gradient-to-r from-blue-500 to-blue-600 text-white px-4 py-2 rounded-xl hover-lift hover-glow flex items-center">
                                <i class="fas fa-exchange-alt mr-2"></i>Transfer
                            </a>
                            <a href="${pageContext.request.contextPath}/history?accountId=${account.accountId}"
                               class="bg-gradient-to-r from-gray-500 to-gray-600 text-white px-4 py-2 rounded-xl hover-lift hover-glow flex items-center">
                                <i class="fas fa-history mr-2"></i>History
                            </a>
                            <form action="${pageContext.request.contextPath}/accountManagement" method="post" class="inline">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="accountId" value="${account.accountId}">
                                <button type="submit" class="bg-gradient-to-r from-red-500 to-red-600 text-white px-4 py-2 rounded-xl hover-lift hover-glow flex items-center"
                                        onclick="return confirm('Are you sure?');">
                                    <i class="fas fa-trash mr-2"></i>Delete
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Add New Account Form -->
    <div class="bg-white bg-opacity-10 backdrop-blur-lg rounded-3xl shadow-2xl p-8 animate-fade-in-scale border border-indigo-500/20">
        <h3 class="text-3xl font-semibold text-white mb-6 tracking-wide">Add New Account</h3>
        <form action="${pageContext.request.contextPath}/accountManagement" method="post" class="space-y-6">
            <input type="hidden" name="action" value="create">
            <input type="hidden" name="userId" value="${sessionScope.user.userId}">

            <div class="relative">
                <label class="block text-indigo-200 font-bold mb-2 text-lg">Account Number</label>
                <input type="text" name="accountNumber" class="w-full p-4 rounded-xl bg-white/5 border border-indigo-500/50 text-white placeholder-indigo-300 focus:border-indigo-400 focus:ring-4 focus:ring-indigo-400/30 transition-all duration-300 hover:bg-white/10" placeholder="Enter your Account Number" required>
                <i class="fas fa-id-card absolute right-4 top-16 text-indigo-400"></i>
            </div>

            <div class="relative">
                <label class="block text-indigo-200 font-bold mb-2 text-lg">Initial Balance</label>
                <input type="number" name="balance" step="0.01" class="w-full p-4 rounded-xl bg-white/5 border border-indigo-500/50 text-white placeholder-indigo-300 focus:border-indigo-400 focus:ring-4 focus:ring-indigo-400/30 transition-all duration-300 hover:bg-white/10" placeholder="Enter your Balance" required>
            </div>

            <button type="submit" class="bg-gradient-to-r from-green-500 to-emerald-600 text-white px-6 py-3 rounded-xl hover-lift hover-glow animate-pulse-glow flex items-center text-lg font-semibold">
                <i class="fas fa-plus-circle mr-3"></i>Add Account
            </button>
        </form>
    </div>
</div>

<!-- Giữ nguyên toàn bộ HTML và CSS trước đó, chỉ thay thế phần script ở cuối -->
<script>
    // Hiệu ứng nhấn nút và hover mượt mà hơn
    document.querySelectorAll('button, a').forEach(element => {
        // Hiệu ứng khi nhấn
        element.addEventListener('mousedown', function() {
            this.style.transition = 'transform 0.15s ease-in-out, opacity 0.15s ease-in-out';
            this.style.transform = 'scale(0.95)';
            this.style.opacity = '0.85';
        });

        element.addEventListener('mouseup', function() {
            this.style.transform = 'scale(1)';
            this.style.opacity = '1';
            // Thêm hiệu ứng bật nhẹ khi thả ra
            this.style.transition = 'transform 0.2s cubic-bezier(0.34, 1.56, 0.64, 1), opacity 0.2s ease-in-out';
            this.style.transform = 'scale(1.02)';
            setTimeout(() => {
                this.style.transform = 'scale(1)';
            }, 150);
        });

        // Hiệu ứng hover
        element.addEventListener('mouseenter', function() {
            if (!this.matches(':active')) { // Chỉ áp dụng khi không nhấn
                this.style.transition = 'transform 0.25s ease-out, opacity 0.25s ease-out';
                this.style.transform = 'scale(1.03)';
                this.style.opacity = '0.95';
            }
        });

        element.addEventListener('mouseleave', function() {
            this.style.transition = 'transform 0.25s ease-in, opacity 0.25s ease-in';
            this.style.transform = 'scale(1)';
            this.style.opacity = '1';
        });
    });

    // Hiệu ứng hover cho input (giữ nguyên nhưng tối ưu hơn)
    document.querySelectorAll('input').forEach(input => {
        input.addEventListener('focus', function() {
            this.style.transition = 'transform 0.2s ease-out';
            this.style.transform = 'scale(1.02)';
        });
        input.addEventListener('blur', function() {
            this.style.transition = 'transform 0.2s ease-in';
            this.style.transform = 'scale(1)';
        });
    });
</script>
</body>
</html>