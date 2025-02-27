package org.example.mvc2jpa.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.example.mvc2jpa.model.User;

import java.util.List;

public class UserDAO {
    private EntityManager em;

    public UserDAO() {
        em = Persistence
                .createEntityManagerFactory("mvcPU")
                .createEntityManager();
    }

    public List<User> getAllUsers() {
     return    em.createQuery("select u from User u", User.class).getResultList();
    }

    public  void  addUser(User user) {
        em.getTransaction().begin(); // start Transaction
        em.persist(user); // thuc thi
        em.getTransaction().commit(); // xac thuc giao dich
    }
}
