<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Add Member</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    .form-container { width: 400px; }
    label { display: block; margin: 10px 0 5px; }
    input { width: 100%; padding: 5px; }
    .error { color: red; }
  </style>
</head>
<body>
<h2>Add New Member</h2>
<% if (request.getAttribute("error") != null) { %>
<p class="error"><%= request.getAttribute("error") %></p>
<% } %>
<div class="form-container">
  <form action="${pageContext.request.contextPath}/members" method="post">
    <label>First Name:</label>
    <input type="text" name="firstName" required>

    <label>Last Name:</label>
    <input type="text" name="lastName" required>

    <label>Email:</label>
    <input type="email" name="email" required>

    <input type="hidden" name="action" value="add">
    <button type="submit">Save</button>
  </form>
</div>
<a href="${pageContext.request.contextPath}/members">Back to List</a>
</body>
</html>