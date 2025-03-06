package com.fptaptech.mvc2jsplibary.controller;

import com.fptaptech.mvc2jsplibary.bean.BookBean;
import com.fptaptech.mvc2jsplibary.dao.BookDAO;
import com.fptaptech.mvc2jsplibary.entity.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/books")
public class BookServlet extends HttpServlet {

    private BookDAO bookDAO;

    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "add":
                request.getRequestDispatcher("/WEB-INF/views/book/add.jsp").forward(request, response);
                break;
            case "edit":
                Long id = Long.parseLong(request.getParameter("id"));
                Book book = bookDAO.findById(id);
                if (book != null) {
                    BookBean bookBean = mapBookToBean(book);
                    request.setAttribute("book", bookBean);
                    request.getRequestDispatcher("/WEB-INF/views/book/edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/books");
                }
                break;
            case "delete":
                bookDAO.delete(Long.parseLong(request.getParameter("id")));
                response.sendRedirect(request.getContextPath() + "/books");
                break;
            default:
                List<BookBean> books = bookDAO.findAll().stream()
                        .map(this::mapBookToBean)
                        .toList();
                request.setAttribute("books", books);
                request.getRequestDispatcher("/WEB-INF/views/book/list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        BookBean bookBean = new BookBean();

        bookBean.setTitle(request.getParameter("title"));
        bookBean.setAuthor(request.getParameter("author"));
        bookBean.setIsbn(request.getParameter("isbn"));
        bookBean.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
        bookBean.setAvailStatus(Integer.parseInt(request.getParameter("availStatus")));
        bookBean.setTotalBook(Integer.parseInt(request.getParameter("totalBook")));

        if ("edit".equals(action)) {
            bookBean.setId(Long.parseLong(request.getParameter("id")));
        }

        bookDAO.save(mapBeanToBook(bookBean));
        response.sendRedirect(request.getContextPath() + "/books");
    }

    private BookBean mapBookToBean(Book book) {
        BookBean bean = new BookBean();
        bean.setId(book.getId());
        bean.setTitle(book.getTitle());
        bean.setAuthor(book.getAuthor());
        bean.setIsbn(book.getIsbn());
        bean.setPublicationYear(0); // Nếu không có dữ liệu từ entity, có thể để mặc định
        bean.setTotalBook(0); // Nếu không có dữ liệu từ entity, có thể để mặc định
        bean.setAvailStatus(book.isAvailable() ? 1 : 0);
        return bean;
    }

    private Book mapBeanToBook(BookBean bean) {
        Book book = new Book();
        book.setId(bean.getId());
        book.setTitle(bean.getTitle());
        book.setAuthor(bean.getAuthor());
        book.setIsbn(bean.getIsbn());
        book.setAvailable(bean.getAvailStatus() == 1);
        return book;
    }
}