package com.fptaptech.demo2.Service;

import com.fptaptech.demo2.DAO.BookDAO;
import com.fptaptech.demo2.DAO.TransactionDAO;
import com.fptaptech.demo2.model.Book;
import com.fptaptech.demo2.model.Transaction;

import java.util.List;
import java.util.Optional;

public class TransactionService {
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final BookDAO bookDAO = new BookDAO();

    public Optional<String> borrowBook(int userId, int bookId) {
        Book book = bookDAO.getAllBooks().stream()
                .filter(b -> b.getId() == bookId)
                .findFirst()
                .orElse(null);

        if (book == null || book.getQuantity() <= 0) {
            return Optional.of("Sách không khả dụng hoặc đã hết.");
        }

        boolean success = transactionDAO.borrowBook(userId, bookId);
        if (success) {
            bookDAO.updateBookQuantity(bookId, book.getQuantity() - 1);
            return Optional.of("Mượn sách thành công! Hạn trả: 14 ngày sau.");
        }
        return Optional.of("Mượn sách thất bại.");
    }

    public Optional<String> returnBook(int transactionId, int bookId) {
        boolean success = transactionDAO.returnBook(transactionId);
        if (success) {
            Book book = bookDAO.getAllBooks().stream()
                    .filter(b -> b.getId() == bookId)
                    .findFirst()
                    .orElse(null);
            if (book != null) {
                bookDAO.updateBookQuantity(bookId, book.getQuantity() + 1);
            }
            return Optional.of("Trả sách thành công!");
        }
        return Optional.of("Trả sách thất bại.");
    }

    public List<Transaction> getUserTransactions(int userId) {
        return transactionDAO.getUserTransactions(userId);
    }
}
