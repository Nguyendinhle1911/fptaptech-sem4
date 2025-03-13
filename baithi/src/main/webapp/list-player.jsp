<%@ page import="java.util.List" %>
<%@ page import="model.Player" %>
<%@ page import="dao.PlayerDAO" %>

<%
    List<Player> players = PlayerDAO.getAllPlayers();
    for (Player player : players) {
%>
<tr>
    <td><%= player.getPlayerId() %></td>
    <td><%= player.getName() %></td>
    <td><%= player.getFullName() %></td>
    <td><%= player.getAge() %></td>
    <td>
        <a href="editPlayer?id=<%= player.getPlayerId() %>">✏️ Sửa</a> |
        <a href="deletePlayer?id=<%= player.getPlayerId() %>" onclick="return confirm('Xác nhận xóa?');">❌ Xóa</a>
    </td>
</tr>
<%
    }
%>
