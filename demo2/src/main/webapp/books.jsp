<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.util.List, com.fptaptech.demo2.model.Book, com.fptaptech.demo2.model.Transaction" %>
<%@ page import="com.fptaptech.demo2.Service.BookService, com.fptaptech.demo2.Service.TransactionService" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    HttpSession sessionUser = request.getSession();
    String username = (String) sessionUser.getAttribute("username");
    int userId = (sessionUser.getAttribute("userId") != null) ? (int) sessionUser.getAttribute("userId") : -1;

    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    BookService bookService = new BookService();
    TransactionService transactionService = new TransactionService();

    List<Book> books = bookService.getAllBooks();
    List<Transaction> transactions = transactionService.getUserTransactions(userId);
    request.setAttribute("books", books);
    request.setAttribute("transactions", transactions);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>📚 Library System</title>

    <!-- ✅ Add Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- ✅ Add FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f8f9fa;
        }
        h2, h3 {
            color: #343a40;
        }
        .table th, .table td {
            text-align: center;
            vertical-align: middle;
        }
        .logout {
            text-decoration: none;
            color: white;
            padding: 8px 12px;
            border-radius: 5px;
            background-color: red;
        }
        .logout:hover {
            background-color: darkred;
        }
        .btn {
            font-size: 14px;
            padding: 6px 12px;
        }
    </style>
</head>
<body class="bg-light">

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="text-primary">📚 Library System</h2>
        <p>Welcome, <strong><c:out value="${username}" /></strong> | <a href="logout" class="btn btn-danger">Logout</a></p>
        <script>
            localStorage.removeItem("jwt_token"); // Xóa JWT khỏi Local Storage
        </script>
    </div>

    <c:if test="${not empty message}">
        <div class="alert ${fn:contains(message, 'failed') ? 'alert-danger' : 'alert-success'}">
            <c:out value="${message}" />
        </div>
    </c:if>

    <h3 class="text-dark">📖 Book List</h3>
    <table class="table table-bordered table-hover">
        <thead class="table-dark">
        <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Quantity</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="book" items="${books}">
            <tr>
                <td><c:out value="${book.title}" /></td>
                <td><c:out value="${book.author}" /></td>
                <td><c:out value="${book.quantity}" /></td>
                <td>
                    <span class="badge ${book.status eq 'AVAILABLE' ? 'bg-success' : 'bg-danger'}">
                        <c:out value="${book.status eq 'AVAILABLE' ? 'Available' : 'Out of stock'}" />
                    </span>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${book.status eq 'AVAILABLE'}">
                            <form action="borrow" method="post">
                                <input type="hidden" name="userId" value="${userId}">
                                <input type="hidden" name="bookId" value="${book.id}">
                                <button type="submit" class="btn btn-primary"><i class="fas fa-book"></i> Borrow</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <span class="text-danger">❌ Out of stock</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <h3 class="text-dark" style="
    margin-top: 60px;
    font-weight: bold;
    font-size: 22px;
    color: #333;
    text-align: center;
    margin-bottom: 20px;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
">
        📜 My Borrowing/Return History
    </h3>

    <c:choose>
        <c:when test="${empty transactions}">
            <div class="alert alert-warning">⚠️ You have not borrowed any books.</div>
        </c:when>
        <c:otherwise>
            <table class="table table-striped">
                <thead class="table-dark">
                <tr>
                    <th>Book Title</th>
                    <th>Borrow Date</th>
                    <th>Due Date</th>
                    <th>Return Date</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="t" items="${transactions}">
                    <tr>
                        <td><c:out value="${t.bookTitle}" /></td>
                        <td><c:out value="${t.borrowDate != null ? t.borrowDate : 'N/A'}" /></td>
                        <td><c:out value="${t.dueDate != null ? t.dueDate : 'N/A'}" /></td>
                        <td><c:out value="${t.returnDate != null ? t.returnDate : 'Not returned'}" /></td>
                        <td>
                            <span class="badge ${t.status eq 'BORROWED' ? 'bg-warning' : 'bg-success'}">
                                <c:out value="${t.status eq 'BORROWED' ? 'Borrowed' : 'Returned'}" />
                            </span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${t.status eq 'BORROWED'}">
                                    <form action="return" method="post">
                                        <input type="hidden" name="transactionId" value="${t.id}">
                                        <input type="hidden" name="bookId" value="${t.bookId}">
                                        <button type="submit" class="btn btn-success"><i class="fas fa-undo"></i> Return</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-success">✅ Returned</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<!-- ✅ Add Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>