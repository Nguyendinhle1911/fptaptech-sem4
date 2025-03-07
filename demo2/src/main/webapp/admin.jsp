<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.fptaptech.demo2.model.Book, com.fptaptech.demo2.model.Transaction" %>
<%@ page import="com.fptaptech.demo2.Service.BookService, com.fptaptech.demo2.Service.TransactionService" %>
<%
    BookService bookService = new BookService();
    TransactionService transactionService = new TransactionService();

    List<Book> books = bookService.getAllBooks();
    List<Transaction> transactions = transactionService.getUserTransactions(-1);
    String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Sách - Admin</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <script src="https://kit.fontawesome.com/YOUR-FONT-AWESOME-KEY.js" crossorigin="anonymous"></script>

    <!-- Custom CSS -->
    <style>
        body {
            background: linear-gradient(120deg, #f6f9fc, #cfd9df);
            font-family: "Poppins", sans-serif;
        }
        .custom-card {
            border-radius: 15px;
            overflow: hidden;
            transition: transform 0.3s ease-in-out;
        }
        .custom-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
        }
        .btn-custom {
            border-radius: 8px;
            transition: all 0.3s ease;
        }
        .btn-custom:hover {
            transform: scale(1.05);
        }
        table {
            background: white;
            border-radius: 10px;
            overflow: hidden;
        }
        table tr:hover {
            background: #f8f9fa;
        }
    </style>
</head>
<body class="bg-light">

<div class="container py-5">

    <!-- HEADER -->
    <div class="text-center mb-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2 class="text-primary">📚 Thư Viện Sách</h2>
            <p>Xin chào, <strong><%= (session.getAttribute("adminName") != null) ? session.getAttribute("adminName") : "Admin" %></strong> |
                <a href="logout" class="btn btn-danger">Đăng xuất</a>
            </p>
        </div>
    </div>
    <!-- THÔNG BÁO -->
    <% if (message != null) { %>
    <div class="alert <%= message.contains("thất bại") ? "alert-danger" : "alert-success" %> text-center">
        <%= message %>
    </div>
    <% } %>

    <!-- FORM THÊM SÁCH -->
    <div class="card shadow-lg custom-card p-4 mb-4">
        <h3 class="text-success"><i class="fas fa-plus-circle"></i> Thêm Sách Mới</h3>
        <form action="addBook" method="post">
            <div class="mb-3">
                <input type="text" name="title" placeholder="Tên sách" required class="form-control">
            </div>
            <div class="mb-3">
                <input type="text" name="author" placeholder="Tác giả" required class="form-control">
            </div>
            <div class="mb-3">
                <input type="number" name="quantity" placeholder="Số lượng" min="1" required class="form-control">
            </div>
            <button type="submit" class="btn btn-success w-100 btn-custom">
                <i class="fas fa-check"></i> Thêm
            </button>
        </form>
    </div>

    <!-- DANH SÁCH SÁCH -->
    <div class="card shadow-lg custom-card p-4 mb-4">
        <h3 class="text-primary"><i class="fas fa-book-open"></i> Danh Sách Sách</h3>
        <% if (books.isEmpty()) { %>
        <p class="text-danger">⚠️ Không có sách nào trong hệ thống.</p>
        <% } else { %>
        <table class="table table-hover">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Tên sách</th>
                <th>Tác giả</th>
                <th>Số lượng</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
            </thead>
            <tbody>
            <% for (Book book : books) { %>
            <tr>
                <td><%= book.getId() %></td>
                <td><%= book.getTitle() %></td>
                <td><%= book.getAuthor() %></td>
                <td><%= book.getQuantity() %></td>
                <td class="<%= book.getStatus().equals("AVAILABLE") ? "text-success" : "text-danger" %>">
                    <%= book.getStatus().equals("AVAILABLE") ? "Còn sách" : "Hết sách" %>
                </td>
                <td>
                    <a href="editBook.jsp?id=<%= book.getId() %>" class="btn btn-warning btn-sm">
                        ✏️ Sửa
                    </a>
                    <a href="deleteBook?id=<%= book.getId() %>" onclick="return confirm('Xóa sách này?');" class="btn btn-danger btn-sm">
                        🗑️ Xóa
                    </a>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <% } %>
    </div>

    <!-- LỊCH SỬ MƯỢN/TRẢ -->
    <div class="card shadow-lg custom-card p-4">
        <h3 class="text-warning"><i class="fas fa-history"></i> Lịch Sử Mượn/Trả</h3>
        <% if (transactions == null || transactions.isEmpty()) { %>
        <p class="text-danger">⚠️ Không có giao dịch nào.</p>
        <% } else { %>
        <table class="table table-hover">
            <thead class="table-dark">
            <tr>
                <th>Người mượn</th>
                <th>Tên sách</th>
                <th>Ngày mượn</th>
                <th>Hạn trả</th>
                <th>Ngày trả</th>
                <th>Trạng thái</th>
            </tr>
            </thead>
            <tbody>
            <% for (Transaction t : transactions) { %>
            <tr>
                <td><%= t.getUserName() %></td>
                <td><%= t.getBookTitle() %></td>
                <td><%= t.getBorrowDate() %></td>
                <td><%= t.getDueDate() != null ? t.getDueDate() : "Không có" %></td>
                <td><%= t.getReturnDate() != null ? t.getReturnDate() : "Chưa trả" %></td>
                <td class="<%= "BORROWED".equals(t.getStatus()) ? "text-warning" : "text-success" %>">
                    <%= "BORROWED".equals(t.getStatus()) ? "Đang mượn" : "Đã trả" %>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <% } %>
    </div>

</div>

</body>
</html>
