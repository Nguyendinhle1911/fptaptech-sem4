<%--
  Created by IntelliJ IDEA.
  User: nguyen
  Date: 15/02/2025
  Time: 11:42 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Product" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product Management</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-dark bg-dark">
    <div class="container-fluid d-flex justify-content-between align-items-center">
        <a class="navbar-brand" href="welcome.jsp"><i class="fa fa-home"></i> Home</a>
        <form method="get" action="logout">
            <button type="submit" class="btn btn-danger btn-custom" onclick="return confirm('Are you sure you want to logout?');">
                <i class="fa fa-sign-out-alt"></i> Logout
            </button>
        </form>
    </div>
</nav>

<!-- Main Container -->
<div class="container d-flex justify-content-center">
    <div class="card w-100">
        <h2 class="text-center mb-4">Manage Products 🛒</h2>

        <!-- Add Product Form -->
        <h4>Add Product</h4>
        <form action="product" method="post" class="row g-3">
            <input type="hidden" name="action" value="add">
            <div class="col-md-5">
                <input type="text" class="form-control" name="name" placeholder="Name" required>
            </div>
            <div class="col-md-4">
                <input type="number" class="form-control" name="quantity" placeholder="Quantity" required>
            </div>
            <div class="col-md-3">
                <button type="submit" class="btn btn-success btn-custom w-100"><i class="fa fa-plus"></i> Add New</button>
            </div>
        </form>

        <!-- Update Product Form -->
        <%
            if (request.getParameter("id") != null) {
        %>
        <h4 class="mt-4">Update Product</h4>
        <form action="product" method="post" class="row g-3">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="<%= request.getParameter("id") %>">
            <div class="col-md-5">
                <input type="text" class="form-control" name="name" value="<%= request.getParameter("name") %>" required>
            </div>
            <div class="col-md-4">
                <input type="number" class="form-control" name="quantity" value="<%= request.getParameter("quantity") %>" required>
            </div>
            <div class="col-md-3">
                <button type="submit" class="btn btn-primary btn-custom w-100"><i class="fa fa-edit"></i> Update</button>
            </div>
        </form>
        <%
            }
        %>

        <!-- Product List Table -->
        <h4 class="mt-4">Product List</h4>
        <table class="table table-hover table-bordered text-center ">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Quantity</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <%
                try {
                    List<Product> products = (List<Product>) request.getAttribute("products");
                    if (products == null) {
                        response.sendRedirect("product");
                        return;
                    }
                    for (Product product : products) {
            %>
            <tr>
                <td><%= product.getId() %></td>
                <td><%= product.getName() %></td>
                <td><%= product.getQuantity() %></td>
                <td>
                    <div class="d-flex justify-content-center">
                        <form action="product" method="get">
                            <input type="hidden" name="id" value="<%= product.getId() %>">
                            <input type="hidden" name="name" value="<%= product.getName() %>">
                            <input type="hidden" name="quantity" value="<%= product.getQuantity() %>">
                            <button type="submit" class="btn btn-warning btn-sm mx-2">
                                <i class="fa fa-edit"></i> Edit
                            </button>
                        </form>
                        <form action="product" method="post">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="<%= product.getId() %>">
                            <button type="submit" class="btn btn-danger btn-sm mx-2" onclick="return confirm('Are you sure you want to delete this product?');">
                                <i class="fa fa-trash"></i> Delete
                            </button>
                        </form>
                    </div>
                </td>

            </tr>
            <%
                    }
                } catch (Exception e) {
                    out.println("<p class='text-danger'>Error: " + e.getMessage() + "</p>");
                }
            %>
            </tbody>
        </table>
    </div>
</div>

<!-- Bootstrap Scripts -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
