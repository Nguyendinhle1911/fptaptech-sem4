package org.example.fetchtypeproblems.service;

import jakarta.transaction.Transactional;
import org.example.fetchtypeproblems.model.Book;
import org.example.fetchtypeproblems.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Book createBook(Book book) {
        if (book.getAuthors() == null || book.getAuthors().isEmpty()) {
            throw new IllegalArgumentException("A book must have at least one author.");
        }
        return bookRepository.save(book);
    }

    @Transactional
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll(); // Lấy tất cả sách từ cơ sở dữ liệu
    }

    @Transactional
    public Book updateBook(Long id, Book updatedBook) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setTitle(updatedBook.getTitle());
            book.setDescription(updatedBook.getDescription());
            book.setAuthors(updatedBook.getAuthors());
            return bookRepository.save(book);
        } else {
            throw new RuntimeException("Book with ID " + id + " not found.");
        }
    }

    @Transactional
    public void deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new RuntimeException("Book with ID " + id + " not found.");
        }
    }
}
