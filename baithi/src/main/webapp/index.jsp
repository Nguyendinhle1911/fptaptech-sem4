<%@ page import="java.util.List" %>
<%@ page import="model.Player" %>
<%@ page import="dao.PlayerDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Player> players = PlayerDAO.getAllPlayers();
    String action = request.getParameter("action");
    Player playerToEdit = null;
    String successMessage = request.getParameter("success");
    String errorMessage = request.getParameter("error");

    try {
        if ("edit".equals(action) && request.getParameter("id") != null) {
            int playerId = Integer.parseInt(request.getParameter("id"));
            playerToEdit = PlayerDAO.getPlayerById(playerId);
            if (playerToEdit == null) {
                errorMessage = "Cầu thủ không tồn tại!";
            }
        }
    } catch (NumberFormatException e) {
        errorMessage = "ID cầu thủ không hợp lệ!";
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Cầu Thủ</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="container mt-5">

<h2 class="text-center mb-4">Quản Lý Cầu Thủ</h2>

<!-- Hiển thị thông báo thành công / lỗi -->
<% if (successMessage != null) { %>
<div class="alert alert-success"><%= successMessage %></div>
<% } %>
<% if (errorMessage != null) { %>
<div class="alert alert-danger"><%= errorMessage %></div>
<% } %>

<!-- Form thêm hoặc sửa cầu thủ -->
<div class="card mb-4">
    <div class="card-header"><%= (playerToEdit == null) ? "Thêm Cầu Thủ" : "Chỉnh Sửa Cầu Thủ" %></div>
    <div class="card-body">
        <form action="<%= (playerToEdit == null) ? "addPlayer" : "updatePlayer" %>" method="post">
            <% if (playerToEdit != null) { %>
            <input type="hidden" name="playerId" value="<%= playerToEdit.getPlayerId() %>">
            <% } %>
            <div class="mb-3">
                <label class="form-label">Tên</label>
                <input type="text" name="name" class="form-control" required value="<%= (playerToEdit != null) ? playerToEdit.getName() : "" %>">
            </div>
            <div class="mb-3">
                <label class="form-label">Họ và Tên</label>
                <input type="text" name="fullName" class="form-control" required value="<%= (playerToEdit != null) ? playerToEdit.getFullName() : "" %>">
            </div>
            <div class="mb-3">
                <label class="form-label">Tuổi</label>
                <input type="number" name="age" class="form-control" required value="<%= (playerToEdit != null) ? playerToEdit.getAge() : "" %>">
            </div>
            <div class="mb-3">
                <label class="form-label">Chỉ Số</label>
                <input type="number" name="indexId" class="form-control" required value="<%= (playerToEdit != null) ? playerToEdit.getIndexId() : "" %>">
            </div>
            <button type="submit" class="btn btn-success">
                <%= (playerToEdit == null) ? "Thêm Cầu Thủ" : "Cập Nhật" %>
            </button>
            <% if (playerToEdit != null) { %>
            <a href="index.jsp" class="btn btn-secondary">Hủy</a>
            <% } %>
        </form>
    </div>
</div>

<!-- Danh sách cầu thủ -->
<h3>Danh Sách Cầu Thủ</h3>
<table class="table table-bordered">
    <thead class="table-dark">
    <tr>
        <th>ID</th>
        <th>Tên</th>
        <th>Họ và Tên</th>
        <th>Tuổi</th>
        <th>Hành Động</th>
    </tr>
    </thead>
    <tbody>
    <% for (Player player : players) { %>
    <tr>
        <td><%= player.getPlayerId() %></td>
        <td><%= player.getName() %></td>
        <td><%= player.getFullName() %></td>
        <td><%= player.getAge() %></td>
        <td>
            <a href="index.jsp?action=edit&id=<%= player.getPlayerId() %>" class="btn btn-warning btn-sm">Sửa</a>
            <a href="deletePlayer?id=<%= player.getPlayerId() %>" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc chắn muốn xóa?');">Xóa</a>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>

</body>
</html>
