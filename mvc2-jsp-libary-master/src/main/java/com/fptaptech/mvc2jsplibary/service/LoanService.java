package com.fptaptech.mvc2jsplibary.service;

import com.fptaptech.mvc2jsplibary.dao.BookDAO;
import com.fptaptech.mvc2jsplibary.dao.LoanDAO;
import com.fptaptech.mvc2jsplibary.dao.MemberDAO;
import com.fptaptech.mvc2jsplibary.entity.Book;
import com.fptaptech.mvc2jsplibary.entity.Loan;
import com.fptaptech.mvc2jsplibary.entity.Member;
import com.fptaptech.mvc2jsplibary.util.DateUtil;

import java.time.LocalDate;
import java.util.List;

public class LoanService {

    private final LoanDAO loanDAO;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;

    // Constructor
    public LoanService() {
        this.loanDAO = new LoanDAO();
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO();
    }

    /**
     * Tìm tất cả các khoản mượn
     */
    public List<Loan> findAllLoans() {
        return loanDAO.findAll();
    }

    /**
     * Tìm khoản mượn theo ID
     */
    public Loan findLoanById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Loan ID must be greater than 0");
        }
        return loanDAO.findById(id);
    }

    /**
     * Tìm các khoản mượn theo ID của thành viên
     */
    public List<Loan> findLoansByMemberId(Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("Member ID must be greater than 0");
        }
        return loanDAO.findByMemberId(memberId);
    }

    /**
     * Tìm các khoản mượn theo ID của sách
     */
    public List<Loan> findLoansByBookId(Long bookId) {
        if (bookId == null || bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be greater than 0");
        }
        return loanDAO.findByBookId(bookId);
    }

    /**
     * Tìm các khoản mượn quá hạn
     */
    public List<Loan> findOverdueLoans() {
        return loanDAO.findOverdueLoans(DateUtil.getCurrentDate());
    }

    /**
     * Thêm một khoản mượn mới
     */
    public void addLoan(Long bookId, Long memberId, LocalDate borrowDate, LocalDate dueDate) {
        // Kiểm tra đầu vào
        if (bookId == null || bookId <= 0 || memberId == null || memberId <= 0 || borrowDate == null || dueDate == null) {
            throw new IllegalArgumentException("Invalid input parameters for loan creation");
        }

        // Kiểm tra ngày hợp lệ
        if (dueDate.isBefore(borrowDate)) {
            throw new IllegalArgumentException("Due date must be after borrow date");
        }

        // Tìm sách và thành viên
        Book book = bookDAO.findById(bookId);
        Member member = memberDAO.findById(memberId);

        if (book == null) {
            throw new IllegalArgumentException("Book not found with ID: " + bookId);
        }
        if (member == null) {
            throw new IllegalArgumentException("Member not found with ID: " + memberId);
        }
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available for borrowing");
        }

        // Tạo khoản mượn mới
        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);
        loan.setBorrowDate(borrowDate);
        loan.setDueDate(dueDate);
        loan.setReturnDate(null); // Chưa trả

        // Cập nhật trạng thái sách thành không khả dụng
        book.setAvailable(false);
        bookDAO.save(book);

        // Lưu khoản mượn
        loanDAO.save(loan);
    }

    /**
     * Cập nhật thông tin khoản mượn (ví dụ: cập nhật ngày trả)
     */
    public void updateLoan(Long loanId, LocalDate returnDate) {
        if (loanId == null || loanId <= 0) {
            throw new IllegalArgumentException("Loan ID must be greater than 0");
        }

        Loan loan = loanDAO.findById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found with ID: " + loanId);
        }

        // Cập nhật ngày trả
        if (returnDate != null) {
            if (returnDate.isBefore(loan.getBorrowDate())) {
                throw new IllegalArgumentException("Return date must be after borrow date");
            }
            loan.setReturnDate(returnDate);

            // Cập nhật trạng thái sách thành khả dụng
            Book book = loan.getBook();
            book.setAvailable(true);
            bookDAO.save(book);
        }

        loanDAO.save(loan);
    }

    /**
     * Xóa một khoản mượn
     */
    public void deleteLoan(Long loanId) {
        if (loanId == null || loanId <= 0) {
            throw new IllegalArgumentException("Loan ID must be greater than 0");
        }

        Loan loan = loanDAO.findById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found with ID: " + loanId);
        }

        // Nếu sách chưa được trả, cần cập nhật trạng thái sách thành khả dụng
        if (loan.getReturnDate() == null) {
            Book book = loan.getBook();
            book.setAvailable(true);
            bookDAO.save(book);
        }

        loanDAO.delete(loanId);
    }

    /**
     * Kiểm tra một khoản mượn có quá hạn hay không
     */
    public boolean isLoanOverdue(Long loanId) {
        if (loanId == null || loanId <= 0) {
            throw new IllegalArgumentException("Loan ID must be greater than 0");
        }

        Loan loan = loanDAO.findById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found with ID: " + loanId);
        }

        return loan.getReturnDate() == null && DateUtil.isOverdue(loan.getDueDate());
    }

    /**
     * Tính số ngày quá hạn của một khoản mượn
     */
    public long getOverdueDays(Long loanId) {
        if (loanId == null || loanId <= 0) {
            throw new IllegalArgumentException("Loan ID must be greater than 0");
        }

        Loan loan = loanDAO.findById(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found with ID: " + loanId);
        }

        if (loan.getReturnDate() == null && DateUtil.isOverdue(loan.getDueDate())) {
            return DateUtil.daysBetween(loan.getDueDate(), DateUtil.getCurrentDate());
        }
        return 0;
    }
}