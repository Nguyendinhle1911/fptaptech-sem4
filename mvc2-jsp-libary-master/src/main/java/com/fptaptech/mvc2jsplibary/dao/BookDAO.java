package com.fptaptech.mvc2jsplibary.dao;

import com.fptaptech.mvc2jsplibary.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class BookDAO{

    @PersistenceContext
    private EntityManager entityManager;

    // Tìm book theo ID
    public Book findById(Long id) {
        return entityManager.find(Book.class, id);
    }

    // Tìm tất cả books
    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b", Book.class);
        return query.getResultList();
    }

    // Lưu hoặc cập nhật book
    public void save(Book book) {
        if (book.getId() == null) {
            entityManager.persist(book);
        } else {
            entityManager.merge(book);
        }
    }

    // Xóa book
    public void delete(Long id) {
        Book book = findById(id);
        if (book != null) {
            entityManager.remove(book);
        }
    }

    // Tìm book theo ISBN
    public Book findByIsbn(String isbn) {
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
        query.setParameter("isbn", isbn);
        return query.getResultStream().findFirst().orElse(null);
    }

    // Tìm books còn available
    public List<Book> findAvailableBooks() {
        TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b WHERE b.available = true", Book.class);
        return query.getResultList();
    }
}