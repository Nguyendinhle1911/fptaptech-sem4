<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #e0eafc, #cfdef3);
            min-height: 100vh;
            font-family: 'Poppins', sans-serif;
        }
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            padding: 30px;
            animation: slideUp 0.6s ease-out;
        }
        h2, h3 {
            font-weight: 600;
            color: #1a73e8;
        }
        .table {
            border-radius: 10px;
            overflow: hidden;
        }
        .table thead {
            background: #1a73e8;
            color: white;
        }
        .btn {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }
        .form-control {
            border-radius: 10px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        .form-control:focus {
            border-color: #1a73e8;
            box-shadow: 0 0 10px rgba(26, 115, 232, 0.2);
        }
        .card-animation {
            animation: fadeIn 0.8s ease-in-out;
        }
        @keyframes slideUp {
            from { transform: translateY(50px); opacity: 0; }
            to { transform: translateY(0); opacity: 1; }
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        .btn-success {
            background: linear-gradient(45deg, #28a745, #34d058);
            border: none;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <c:if test="${empty sessionScope.user}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <h2 class="text-primary mb-4 animate__animated animate__fadeInDown">
        <i class="fas fa-user-circle me-2"></i>Welcome, ${sessionScope.user.username}
    </h2>

    <h3 class="mt-4 animate__animated animate__fadeIn">Your Accounts</h3>
    <div class="table-responsive card-animation">
        <table class="table table-bordered table-striped">
            <thead class="table-dark">
            <tr>
                <th>Account Number</th>
                <th>Balance</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="account" items="${sessionScope.accounts}">
                <tr class="animate__animated animate__fadeInUp" style="animation-delay: ${status.index * 0.1}s;">
                    <td>${account.accountNumber}</td>
                    <td class="fw-bold text-success">$${account.balance}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/withdraw.jsp?accountId=${account.accountId}"
                           class="btn btn-warning btn-sm me-2"><i class="fas fa-money-bill-wave"></i> Withdraw</a>
                        <a href="${pageContext.request.contextPath}/transfer.jsp?accountId=${account.accountId}"
                           class="btn btn-info btn-sm me-2"><i class="fas fa-exchange-alt"></i> Transfer</a>
                        <a href="${pageContext.request.contextPath}/history?accountId=${account.accountId}"
                           class="btn btn-secondary btn-sm me-2"><i class="fas fa-history"></i> History</a>
                        <form action="${pageContext.request.contextPath}/accountManagement" method="post" class="d-inline">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="accountId" value="${account.accountId}">
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?');">
                                <i class="fas fa-trash"></i> Delete
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <h3 class="mt-4 animate__animated animate__fadeIn">Add New Account</h3>
    <form action="${pageContext.request.contextPath}/accountManagement" method="post" class="mt-3 card-animation">
        <input type="hidden" name="action" value="create">
        <input type="hidden" name="userId" value="${sessionScope.user.userId}">

        <div class="mb-3">
            <label class="form-label fw-bold">Account Number</label>
            <input type="text" name="accountNumber" class="form-control" placeholder="Enter your Account Number" required>
        </div>

        <div class="mb-3">
            <label class="form-label fw-bold">Initial Balance</label>
            <input type="number" name="balance" class="form-control" step="0.01" placeholder="Enter your Balance" required>
        </div>

        <button type="submit" class="btn btn-success"><i class="fas fa-plus-circle me-2"></i>Add Account</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.js"></script>
<script>
    document.querySelectorAll('.btn').forEach(btn => {
        btn.addEventListener('click', function() {
            this.classList.add('animate__animated', 'animate__pulse');
            setTimeout(() => this.classList.remove('animate__animated', 'animate__pulse'), 500);
        });
    });
</script>
</body>
</html>