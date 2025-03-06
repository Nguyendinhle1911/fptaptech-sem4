<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Loan</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .form-container { width: 400px; }
        label { display: block; margin: 10px 0 5px; }
        input, select { width: 100%; padding: 5px; }
    </style>
</head>
<body>
<h2>Edit Loan</h2>
<div class="form-container">
    <form action="${pageContext.request.contextPath}/loans" method="post">
        <input type="hidden" name="id" value="${loan.id}">

        <label>Book:</label>
        <select name="bookId" required>
            <c:forEach var="book" items="${books}">
                <option value="${book.id}" ${book.id == loan.bookId ? 'selected' : ''}>${book.title}</option>
            </c:forEach>
        </select>

        <label>Member:</label>
        <select name="memberId" required>
            <c:forEach var="member" items="${members}">
                <option value="${member.id}" ${member.id == loan.memberId ? 'selected' : ''}>
                        ${member.firstName} ${member.lastName}
                </option>
            </c:forEach>
        </select>

        <label>Borrow Date:</label>
        <input type="date" name="borrowDate" value="${loan.borrowDate}" required>

        <label>Due Date:</label>
        <input type="date" name="dueDate" value="${loan.dueDate}" required>

        <label>Return Date:</label>
        <input type="date" name="returnDate" value="${loan.returnDate}">

        <input type="hidden" name="action" value="edit">
        <button type="submit">Update</button>
    </form>
</div>
<a href="${pageContext.request.contextPath}/loans">Back to List</a>
</body>
</html>