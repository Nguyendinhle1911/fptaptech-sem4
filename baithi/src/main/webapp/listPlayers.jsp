<!DOCTYPE html>
<html>
<head>
    <title>List Players</title>
</head>
<body>
<h1>Player List</h1>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Full Name</th>
        <th>Age</th>
        <th>Index ID</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="player" items="${players}">
        <tr>
            <td>${player.playerId}</td>
            <td>${player.name}</td>
            <td>${player.fullName}</td>
            <td>${player.age}</td>
            <td>${player.indexId}</td>
            <td>
                <a href="editPlayer?id=${player.playerId}">Edit</a>
                <a href="deletePlayer?id=${player.playerId}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="addPlayer.jsp">Add New Player</a>
</body>
</html>