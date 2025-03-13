<!DOCTYPE html>
<html>
<head>
    <title>Edit Player</title>
</head>
<body>
<h1>Edit Player</h1>
<form action="editPlayer" method="post">
    <input type="hidden" name="playerId" value="${player.playerId}">
    Name: <input type="text" name="name" value="${player.name}"><br>
    Full Name: <input type="text" name="fullName" value="${player.fullName}"><br>
    Age: <input type="text" name="age" value="${player.age}"><br>
    Index ID: <input type="text" name="indexId" value="${player.indexId}"><br>
    <input type="submit" value="Update Player">
</form>
<a href="listPlayers">Back to List</a>
</body>
</html>