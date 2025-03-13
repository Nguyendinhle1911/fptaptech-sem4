<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Edit Player</title>
</head>
<body>
<h1>Edit Player</h1>
<c:if test="${not empty message}">
    <p style="color: green;">${message}</p>
</c:if>
<form action="${pageContext.request.contextPath}/edit" method="post">
    <input type="hidden" name="playerId" value="${player.id}">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" value="${player.playerName}" required><br>
    <label for="fullName">Full Name:</label>
    <input type="text" id="fullName" name="fullName" value="${player.fullName}" required><br>
    <label for="age">Age:</label>
    <input type="number" id="age" name="age" value="${player.age}" required><br>
    <label for="indexId">Index ID:</label>
    <input type="number" id="indexId" name="indexId" value="1" required><br> <!-- Thay bằng giá trị thực tế -->
    <label for="value">Value:</label>
    <input type="number" step="0.01" id="value" name="value" value="${player.value}" required><br>
    <button type="submit">Update Player</button>
</form>
<a href="${pageContext.request.contextPath}/player">Back to Player List</a>
</body>
</html>