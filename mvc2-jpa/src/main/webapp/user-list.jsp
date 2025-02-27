<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
  <title>Danh sách người dùng</title>
  <style>
    table { border-collapse: collapse; width: 80%; margin: 20px auto; }
    th, td { border: 1px solid black; padding: 8px; text-align: left; }
    th { background-color: #f2f2f2; }
    form { width: 50%; margin: 20px auto; }
  </style>
</head>
<body>
<h1>Danh sách người dùng</h1>
<table>
  <tr>
    <th>ID</th>
    <th>Tên người dùng</th>
    <th>Email</th>
  </tr>
  <c:forEach var="user" items="${requestScope.users}">
    <tr>
      <td>${user.id}</td>
      <td>${user.name}</td>
      <td>${user.email}</td>
    </tr>
  </c:forEach>
</table>

<h2>Thêm người dùng mới</h2>
<form action="users" method="post">
  <label for="username">Tên người dùng:</label><br>
  <input type="text" id="username" name="username" required><br><br>
  <label for="email">Email:</label><br>
  <input type="email" id="email" name="email" required><br><br>
  <input type="submit" value="Thêm">
</form>
</body>
</html>