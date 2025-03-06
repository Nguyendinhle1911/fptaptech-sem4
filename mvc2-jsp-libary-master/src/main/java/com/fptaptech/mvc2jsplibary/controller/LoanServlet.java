package com.fptaptech.mvc2jsplibary.controller;

import com.fptaptech.mvc2jsplibary.bean.LoanBean;
import com.fptaptech.mvc2jsplibary.dao.BookDAO;
import com.fptaptech.mvc2jsplibary.dao.LoanDAO;
import com.fptaptech.mvc2jsplibary.dao.MemberDAO;
import com.fptaptech.mvc2jsplibary.entity.Loan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/loans")
public class LoanServlet extends HttpServlet {

    private LoanDAO loanDAO;
    private BookDAO bookDAO;
    private MemberDAO memberDAO;

    @Override
    public void init() throws ServletException {
        loanDAO = new LoanDAO();
        bookDAO = new BookDAO();
        memberDAO = new MemberDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "add":
                request.setAttribute("books", bookDAO.findAvailableBooks());
                request.setAttribute("members", memberDAO.findAll());
                request.getRequestDispatcher("/WEB-INF/views/loan/add.jsp").forward(request, response);
                break;
            case "edit":
                Long id = Long.parseLong(request.getParameter("id"));
                Loan loan = loanDAO.findById(id);
                if (loan != null) {
                    LoanBean loanBean = mapLoanToBean(loan);
                    request.setAttribute("loan", loanBean);
                    request.setAttribute("books", bookDAO.findAll());
                    request.setAttribute("members", memberDAO.findAll());
                    request.getRequestDispatcher("/WEB-INF/views/loan/edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/loans");
                }
                break;
            case "delete":
                loanDAO.delete(Long.parseLong(request.getParameter("id")));
                response.sendRedirect(request.getContextPath() + "/loans");
                break;
            default:
                List<LoanBean> loans = loanDAO.findAll().stream()
                        .map(this::mapLoanToBean)
                        .toList();
                request.setAttribute("loans", loans);
                request.getRequestDispatcher("/WEB-INF/views/loan/list.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        LoanBean loanBean = new LoanBean();

        loanBean.setBookId(Long.parseLong(request.getParameter("bookId")));
        loanBean.setMemberId(Long.parseLong(request.getParameter("memberId")));
        loanBean.setBorrowDate(LocalDate.parse(request.getParameter("borrowDate")));
        loanBean.setDueDate(LocalDate.parse(request.getParameter("dueDate")));
        String returnDate = request.getParameter("returnDate");
        if (returnDate != null && !returnDate.isEmpty()) {
            loanBean.setReturnDate(LocalDate.parse(returnDate));
        }

        if ("edit".equals(action)) {
            loanBean.setId(Long.parseLong(request.getParameter("id")));
        }

        loanDAO.save(mapBeanToLoan(loanBean));
        response.sendRedirect(request.getContextPath() + "/loans");
    }

    private LoanBean mapLoanToBean(Loan loan) {
        LoanBean bean = new LoanBean();
        bean.setId(loan.getId());
        bean.setBookId(loan.getBook().getId());
        bean.setMemberId(loan.getMember().getId());
        bean.setBookTitle(loan.getBook().getTitle());
        bean.setMemberName(loan.getMember().getFirstName() + " " + loan.getMember().getLastName());
        bean.setBorrowDate(loan.getBorrowDate());
        bean.setDueDate(loan.getDueDate());
        bean.setReturnDate(loan.getReturnDate());
        if (loan.getReturnDate() == null && LocalDate.now().isAfter(loan.getDueDate())) {
            bean.setOverdueDays((int) (LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay()));
        }
        return bean;
    }

    private Loan mapBeanToLoan(LoanBean bean) {
        Loan loan = new Loan();
        loan.setId(bean.getId());
        loan.setBook(bookDAO.findById(bean.getBookId()));
        loan.setMember(memberDAO.findById(bean.getMemberId()));
        loan.setBorrowDate(bean.getBorrowDate());
        loan.setDueDate(bean.getDueDate());
        loan.setReturnDate(bean.getReturnDate());
        return loan;
    }
}