<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.example.demo2.utils.DatabaseConnection" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Player Information</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            padding: 8px;
        }
        th {
            background-color: #f2a154;
        }
        .form-group {
            margin: 10px 0;
        }
        .error {
            color: red;
        }
    </style>
</head>
<body>
<h2>Player Information</h2>
<div class="form-group">
    <input type="text" id="playerName" name="playerName" placeholder="Player name">
    <input type="text" id="playerAge" name="playerAge" placeholder="Player age">
    <select id="indexName" name="indexName">
        <option value="speed">speed</option>
        <option value="strength">strength</option>
        <option value="accurate">accurate</option>
    </select>
    <input type="text" id="value" name="value" placeholder="Value">
    <button onclick="addPlayer()">Add</button>
</div>
<c:if test="${param.error == 'validation'}">
    <p class="error">Validation failed! Check value range.</p>
</c:if>
<c:if test="${param.error == '1'}">
    <p class="error">Error occurred!</p>
</c:if>

<table id="playerTable">
    <tr>
        <th>Id</th>
        <th>Player name</th>
        <th>Player age</th>
        <th>Index name</th>
        <th>Value</th>
        <th>Actions</th>
    </tr>
    <%
        Connection conn = DatabaseConnection.getConnection();
        String sql = "SELECT p.player_id, p.name, p.age, i.name AS index_name, pi.value " +
                "FROM player p " +
                "JOIN player_index pi ON p.player_id = pi.player_id " +
                "JOIN indexer i ON pi.index_id = i.index_id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        request.setAttribute("players", rs);
    %>
    <c:forEach var="player" items="${players}">
        <tr>
            <td><c:out value="${player.player_id}" /></td>
            <td><c:out value="${player.name}" /></td>
            <td><c:out value="${player.age}" /></td>
            <td><c:out value="${player.index_name}" /></td>
            <td><c:out value="${player.value}" /></td>
            <td>
                <a href="edit?id=<c:out value="${player.player_id}" />"><i>✏️</i></a>
                <a href="delete?id=<c:out value="${player.player_id}" />"><i>🗑️</i></a>
            </td>
        </tr>
    </c:forEach>
    <% conn.close(); %>
</table>

<script>
    function addPlayer() {
        var form = document.createElement("form");
        form.method = "post";
        form.action = "addPlayer";

        var inputs = ["playerName", "playerAge", "indexName", "value"];
        inputs.forEach(function(id) {
            var input = document.createElement("input");
            input.type = "hidden";
            input.name = id;
            input.value = document.getElementById(id).value;
            form.appendChild(input);
        });

        document.body.appendChild(form);
        form.submit();
    }
</script>
</body>
</html>