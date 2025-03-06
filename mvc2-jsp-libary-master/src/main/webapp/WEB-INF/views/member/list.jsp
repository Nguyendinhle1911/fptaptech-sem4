<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Member List</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #f2f2f2; }
        .button { padding: 5px 10px; text-decoration: none; }
    </style>
</head>
<body>
<h2>Member List</h2>
<a href="${pageContext.request.contextPath}/members?action=add" class="button">Add New Member</a>
<table>
    <tr>
        <th>ID</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Email</th>
        <th>Total Borrowed</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="member" items="${members}">
        <tr>
            <td>${member.id}</td>
            <td>${member.firstName}</td>
            <td>${member.lastName}</td>
            <td>${member.email}</td>
            <td>${member.totalBorrowedBooks}</td>
            <td>
                <a href="${pageContext.request.contextPath}/members?action=edit&id=${member.id}" class="button">Edit</a>
                <a href="${pageContext.request.contextPath}/members?action=delete&id=${member.id}"
                   class="button" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/auth?action=logout" class="button">Logout</a>
</body>
</html>