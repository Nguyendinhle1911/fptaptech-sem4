package com.fptaptech.mvc2jsplibary.dao;

import com.fptaptech.mvc2jsplibary.entity.Loan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class LoanDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // Tìm loan theo ID
    public Loan findById(Long id) {
        return entityManager.find(Loan.class, id);
    }

    // Tìm tất cả loans
    public List<Loan> findAll() {
        TypedQuery<Loan> query = entityManager.createQuery("SELECT l FROM Loan l", Loan.class);
        return query.getResultList();
    }

    // Lưu hoặc cập nhật loan
    public void save(Loan loan) {
        if (loan.getId() == null) {
            entityManager.persist(loan);
        } else {
            entityManager.merge(loan);
        }
    }

    // Xóa loan
    public void delete(Long id) {
        Loan loan = findById(id);
        if (loan != null) {
            entityManager.remove(loan);
        }
    }

    // Tìm loans theo member id
    public List<Loan> findByMemberId(Long memberId) {
        TypedQuery<Loan> query = entityManager.createQuery("SELECT l FROM Loan l WHERE l.member.id = :memberId", Loan.class);
        query.setParameter("memberId", memberId);
        return query.getResultList();
    }

    // Tìm loans theo book id
    public List<Loan> findByBookId(Long bookId) {
        TypedQuery<Loan> query = entityManager.createQuery("SELECT l FROM Loan l WHERE l.book.id = :bookId", Loan.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    // Tìm loans quá hạn
    public List<Loan> findOverdueLoans(LocalDate currentDate) {
        TypedQuery<Loan> query = entityManager.createQuery(
                "SELECT l FROM Loan l WHERE l.returnDate IS NULL AND l.dueDate < :currentDate", Loan.class);
        query.setParameter("currentDate", currentDate);
        return query.getResultList();
    }
}