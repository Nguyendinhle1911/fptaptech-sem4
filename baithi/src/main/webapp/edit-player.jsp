<%@ page import="model.Player" %>
<%
  Player player = (Player) request.getAttribute("player");
  if (player == null) {
    response.sendRedirect("index.jsp");
    return;
  }
%>
<html>
<head>
  <title>Sửa Cầu Thủ</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2>Sửa Cầu Thủ</h2>
<form action="updatePlayer" method="post">
  <input type="hidden" name="playerId" value="<%= player.getPlayerId() %>">
  <label>Tên:</label> <input type="text" name="name" value="<%= player.getName() %>" required><br>
  <label>Họ và Tên:</label> <input type="text" name="fullName" value="<%= player.getFullName() %>" required><br>
  <label>Tuổi:</label> <input type="number" name="age" value="<%= player.getAge() %>" required><br>
  <label>Chỉ Số:</label> <input type="number" name="indexId" value="<%= player.getIndexId() %>" required><br>
  <button type="submit">Cập Nhật</button>
</form>
<a href="index.jsp">🔙 Quay lại</a>
</body>
</html>
