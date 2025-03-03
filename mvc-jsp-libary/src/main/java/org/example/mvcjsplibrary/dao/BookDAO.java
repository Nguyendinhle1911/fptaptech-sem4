package org.example.mvcjsplibrary.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.mvcjsplibrary.entity.Book;

import java.util.List;

public class BookDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional//ACID
    public void saveBook(Book book) {
        entityManager.persist(book);
    }
    public Book findBookById(Long id) {
        return entityManager.find(Book.class, id);
    }

//    public List<Book> getAllBook() {
//        return entityManager
//    }
    @Transactional
    public void updateAvailBook(Long BookId, int newAvail) {
        Book book = findBookById(BookId);
        if (book != null) {
            book.setTotalBook(newAvail);
            entityManager.merge(book);
        }
    }
}
