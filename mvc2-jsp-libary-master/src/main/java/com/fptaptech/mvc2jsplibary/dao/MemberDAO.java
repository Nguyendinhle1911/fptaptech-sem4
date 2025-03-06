package com.fptaptech.mvc2jsplibary.dao;

import com.fptaptech.mvc2jsplibary.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class MemberDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // Tìm member theo ID
    public Member findById(Long id) {
        return entityManager.find(Member.class, id);
    }

    // Tìm tất cả members
    public List<Member> findAll() {
        TypedQuery<Member> query = entityManager.createQuery("SELECT m FROM Member m", Member.class);
        return query.getResultList();
    }

    // Lưu hoặc cập nhật member
    public void save(Member member) {
        if (member.getId() == null) {
            entityManager.persist(member);
        } else {
            entityManager.merge(member);
        }
    }

    // Xóa member
    public void delete(Long id) {
        Member member = findById(id);
        if (member != null) {
            entityManager.remove(member);
        }
    }

    // Tìm member theo email
    public Member findByEmail(String email) {
        TypedQuery<Member> query = entityManager.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst().orElse(null);
    }
}