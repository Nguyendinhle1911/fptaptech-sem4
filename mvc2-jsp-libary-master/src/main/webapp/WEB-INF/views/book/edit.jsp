<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Book</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-container { width: 400px; }
        label { display: block; margin: 10px 0 5px; }
        input, select { width: 100%; padding: 5px; }
    </style>
</head>
<body>
<h2>Edit Book</h2>
<div class="form-container">
    <form action="${pageContext.request.contextPath}/books" method="post">
        <input type="hidden" name="id" value="${book.id}">

        <label>Title:</label>
        <input type="text" name="title" value="${book.title}" required>

        <label>Author:</label>
        <input type="text" name="author" value="${book.author}">

        <label>ISBN:</label>
        <input type="text" name="isbn" value="${book.isbn}">

        <label>Available:</label>
        <select name="availStatus">
            <option value="1" ${book.availStatus == 1 ? 'selected' : ''}>Yes</option>
            <option value="0" ${book.availStatus == 0 ? 'selected' : ''}>No</option>
        </select>

        <input type="hidden" name="action" value="edit">
        <button type="submit">Update</button>
    </form>
</div>
<a href="${pageContext.request.contextPath}/books">Back to List</a>
</body>
</html>