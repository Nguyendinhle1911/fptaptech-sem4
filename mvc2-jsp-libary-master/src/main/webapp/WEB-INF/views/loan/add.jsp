<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Add Loan</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    .form-container { width: 400px; }
    label { display: block; margin: 10px 0 5px; }
    input, select { width: 100%; padding: 5px; }
    .error { color: red; }
  </style>
</head>
<body>
<h2>Add New Loan</h2>
<% if (request.getAttribute("error") != null) { %>
<p class="error"><%= request.getAttribute("error") %></p>
<% } %>
<div class="form-container">
  <form action="${pageContext.request.contextPath}/loans" method="post">
    <label>Book:</label>
    <select name="bookId" required>
      <c:forEach var="book" items="${books}">
        <option value="${book.id}">${book.title}</option>
      </c:forEach>
    </select>

    <label>Member:</label>
    <select name="memberId" required>
      <c:forEach var="member" items="${members}">
        <option value="${member.id}">${member.firstName} ${member.lastName}</option>
      </c:forEach>
    </select>

    <label>Borrow Date:</label>
    <input type="date" name="borrowDate" required>

    <label>Due Date:</label>
    <input type="date" name="dueDate" required>

    <input type="hidden" name="action" value="add">
    <button type="submit">Save</button>
  </form>
</div>
<a href="${pageContext.request.contextPath}/loans">Back to List</a>
</body>
</html>