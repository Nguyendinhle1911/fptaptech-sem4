<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Transaction History</title>
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
        h2 {
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
        .table tbody tr {
            transition: background-color 0.3s ease, transform 0.3s ease;
        }
        .table tbody tr:hover {
            background-color: #f1f5f9;
            transform: scale(1.02);
        }
        .btn-primary {
            background: linear-gradient(45deg, #1a73e8, #4285f4);
            border: none;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .btn-primary:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
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
    </style>
</head>
<body>
<div class="container mt-5">
    <c:if test="${empty sessionScope.user}">
        <c:redirect url="login.jsp"/>
    </c:if>

    <h2 class="text-primary mb-4 animate__animated animate__fadeInDown">
        <i class="fas fa-history me-2"></i>Transaction History
    </h2>

    <div class="table-responsive card-animation">
        <table class="table table-bordered table-striped table-hover">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Amount</th>
                <th>Target Account ID</th>
                <th>Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="transaction" items="${requestScope.transactions}">
                <tr class="animate__animated animate__fadeInUp" style="animation-delay: ${status.index * 0.1}s;">
                    <td>${transaction.transactionId}</td>
                    <td>
                        <c:choose>
                            <c:when test="${transaction.type == 'DEPOSIT'}">
                                <span class="badge bg-success"><i class="fas fa-arrow-down me-1"></i>Deposit</span>
                            </c:when>
                            <c:when test="${transaction.type == 'WITHDRAW'}">
                                <span class="badge bg-warning"><i class="fas fa-arrow-up me-1"></i>Withdraw</span>
                            </c:when>
                            <c:when test="${transaction.type == 'TRANSFER'}">
                                <span class="badge bg-info"><i class="fas fa-exchange-alt me-1"></i>Transfer</span>
                            </c:when>
                            <c:otherwise>
                                ${transaction.type}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td class="fw-bold text-success">$${transaction.amount}</td>
                    <td><c:out value="${transaction.targetAccountId != null ? transaction.targetAccountId : 'N/A'}" /></td>
                    <td>${transaction.transactionDate}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <a href="${pageContext.request.contextPath}/dashboard.jsp" class="btn btn-primary mt-3 animate__animated animate__fadeIn">
        <i class="fas fa-arrow-left me-2"></i>Back to Dashboard
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