<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Book</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-container { width: 400px; }
        label { display: block; margin: 10px 0 5px; }
        input, select { width: 100%; padding: 5px; }
        .error { color: red; }
    </style>
</head>
<body>
<h2>Add New Book</h2>
<% if (request.getAttribute("error") != null) { %>
<p class="error"><%= request.getAttribute("error") %></p>
<% } %>
<div class="form-container">
    <form action="${pageContext.request.contextPath}/books" method="post">
        <label>Title:</label>
        <input type="text" name="title" required>

        <label>Author:</label>
        <input type="text" name="author">

        <label>ISBN:</label>
        <input type="text" name="isbn">

        <label>Available:</label>
        <select name="availStatus">
            <option value="1">Yes</option>
            <option value="0">No</option>
        </select>

        <input type="hidden" name="action" value="add">
        <button type="submit">Save</button>
    </form>
</div>
<a href="${pageContext.request.contextPath}/books">Back to List</a>
</body>
</html>