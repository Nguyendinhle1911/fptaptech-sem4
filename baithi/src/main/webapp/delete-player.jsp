<%
    int playerId = Integer.parseInt(request.getParameter("id"));
%>
<html>
<head>
    <title>Xóa Cầu Thủ</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h2>Xóa Cầu Thủ</h2>
<p>Bạn có chắc chắn muốn xóa cầu thủ có ID <%= playerId %> không?</p>
<a href="deletePlayer?id=<%= playerId %>">✅ Đồng Ý</a> |
<a href="index.jsp">❌ Hủy</a>
</body>
</html>
