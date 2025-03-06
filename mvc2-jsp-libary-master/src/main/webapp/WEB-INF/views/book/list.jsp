<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book List</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #f2f2f2; }
        .button { padding: 5px 10px; text-decoration: none; }
    </style>
</head>
<body>
<h2>Book List</h2>
<a href="${pageContext.request.contextPath}/books?action=add" class="button">Add New Book</a>
<table>
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Author</th>
        <th>ISBN</th>
        <th>Available</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="book" items="${books}">
        <tr>
            <td>${book.id}</td>
            <td>${book.title}</td>
            <td>${book.author}</td>
            <td>${book.isbn}</td>
            <td>${book.availStatus == 1 ? 'Yes' : 'No'}</td>
            <td>
                <a href="${pageContext.request.contextPath}/books?action=edit&id=${book.id}" class="button">Edit</a>
                <a href="${pageContext.request.contextPath}/books?action=delete&id=${book.id}"
                   class="button" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="${pageContext.request.contextPath}/auth?action=logout" class="button">Logout</a>
</body>
</html>