<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Player</title>
    <style>
        .form-group {
            margin: 10px 0;
        }
        .error {
            color: red;
        }
    </style>
</head>
<body>
<h2>Edit Player</h2>
<c:if test="${param.error == 'validation'}">
    <p class="error">Validation failed! Check value range.</p>
</c:if>
<c:if test="${param.error == '1'}">
    <p class="error">Error occurred!</p>
</c:if>

<form method="post" action="edit">
    <input type="hidden" name="id" value="${player.playerId}">
    <div class="form-group">
        <input type="text" name="playerName" value="${player.name}" placeholder="Player name">
        <input type="text" name="playerAge" value="${player.age}" placeholder="Player age">
        <select name="indexName">
            <option value="speed" ${player.indexId == 1 ? 'selected' : ''}>speed</option>
            <option value="strength" ${player.indexId == 2 ? 'selected' : ''}>strength</option>
            <option value="accurate" ${player.indexId == 3 ? 'selected' : ''}>accurate</option>
        </select>
        <input type="text" name="value" placeholder="Value">
        <button type="submit">Save</button>
    </div>
</form>
</body>
</html>