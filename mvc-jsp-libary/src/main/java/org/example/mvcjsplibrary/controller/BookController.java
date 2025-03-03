package org.example.mvcjsplibrary.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.mvcjsplibrary.bean.BookBean;
import org.example.mvcjsplibrary.dao.BookDAO;
import org.example.mvcjsplibrary.entity.Book;

import java.io.IOException;

@WebServlet("/book")
public class BookController extends HttpServlet {

    private BookDAO bookDAO = new BookDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
//        Long id = Long.parseLong(req.getParameter("id"));
//        req.setAttribute("book",bookDAO.findBookById(id));
        String action = req.getParameter("action");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        String action = req.getParameter("action");
        if("add".equals(action)) {
            BookBean bookBean = new BookBean();
            bookBean.setTitle(req.getParameter("title"));
            bookBean.setAuthor(req.getParameter("author"));
            // chuyen doi BooBean sang Book Entity
            Book book = new Book();
            book.setTitle(bookBean.getTitle());
            book.setAuthor(bookBean.getAuthor());
            bookDAO.saveBook(book);
            resp.sendRedirect("/book?action=list"); // chuyen sang 1 sevrlet hoac trong 1 sevetlet khac
            // req.getRequestDispatcher("list.jsp").forward(req, resp);
        }
    }
}
