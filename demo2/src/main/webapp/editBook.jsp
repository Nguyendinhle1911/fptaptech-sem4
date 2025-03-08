<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.fptaptech.demo2.model.Book" %>
<%@ page import="com.fptaptech.demo2.Service.BookService" %>

<%
    // Get book ID from request
    int bookId = Integer.parseInt(request.getParameter("id"));

    // Retrieve book information from database
    BookService bookService = new BookService();
    Book book = bookService.getBookById(bookId);

    if (book == null) {
        response.sendRedirect("admin.jsp"); // Redirect to the list if the book is not found
        return;
    }

    request.setAttribute("book", book); // Đặt đối tượng book vào request scope để sử dụng với JSTL
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>✏️ Edit Book</title>

    <!-- ✅ Add Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- ✅ Add FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f8f9fa;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }
        .container {
            max-width: 600px;
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0px 0px 20px rgba(0, 0, 0, 0.1);
            margin: 20px;
        }
        h2 {
            color: #007bff;
            text-align: center;
            margin-bottom: 20px;
        }
        label {
            font-weight: bold;
            margin-top: 10px;
            color: #495057;
        }
        .form-control {
            margin-bottom: 15px;
            border-radius: 5px;
            border: 1px solid #ced4da;
            padding: 10px;
            font-size: 16px;
        }
        .form-control:focus {
            border-color: #80bdff;
            outline: 0;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
        }
        .btn-primary {
            width: 100%;
            font-size: 16px;
            padding: 10px;
            border-radius: 5px;
            background-color: #007bff;
            border: none;
            transition: background-color 0.3s ease;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        .back a {
            display: block;
            text-align: center;
            margin-top: 15px;
            text-decoration: none;
            color: #007bff;
            transition: color 0.3s ease;
        }
        .back a:hover {
            color: #0056b3;
            text-decoration: underline;
        }
        .icon {
            margin-right: 8px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>✏️ Edit Book</h2>
    <form action="editBook" method="post">
        <input type="hidden" name="id" value="${book.id}">

        <label for="title">Book Title:</label>
        <input type="text" id="title" name="title" class="form-control" value="${book.title}" required>

        <label for="author">Author:</label>
        <input type="text" id="author" name="author" class="form-control" value="${book.author}" required>

        <label for="quantity">Quantity:</label>
        <input type="number" id="quantity" name="quantity" class="form-control" value="${book.quantity}" min="0" required>

        <button type="submit" class="btn btn-primary">
            <i class="fas fa-save icon"></i> Update
        </button>
    </form>

    <div class="back">
        <a href="admin.jsp">
            <i class="fas fa-arrow-left icon"></i> Back to list
        </a>
    </div>
</div>

<!-- ✅ Add Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>