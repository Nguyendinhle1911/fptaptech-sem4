<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Loan List</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #f2f2f2; }
        .button { padding: 5px 10px; text-decoration: none; }
        .overdue { color: red; }
    </style>
</head>
<body>
<h2>Loan List</h2>
<a href="${pageContext.request.contextPath}/loans?action=add" class="button">Add New Loan</a>
<table>
    <tr>
        <th>ID</th>
        <th>Book</th>
        <th>Member</th>
        <th>Borrow Date</th>
        <th>Due Date</th>
        <th>Return Date</th>
        <th>Overdue Days</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="loan" items="${loans}">
        <tr>
            <td>${loan.id}</td>
            <td>${loan.bookTitle}</td>
            <td>${loan.memberName}</td>
            <td>${loan.borrowDate}</td>
            <td>${loan.dueDate}</td>
            <td>${loan.returnDate}</td>
            <td class="${loan.overdueDays > 0 ? 'overdue' : ''}">${loan.overdueDays > 0 ? loan.overdueDays : ''}</td>
            <td>
                <a href="${pageContext.request.contextPath}/loans?action=edit&id=${loan.id}" class="button">Edit</a>
                <a href="${pageContext.request.contextPath}/loans?action=delete&id=${loan.id}"
                   class="button" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/auth?action=logout" class="button">Logout</a>
</body>
</html>