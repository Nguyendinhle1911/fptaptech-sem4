package org.example.springbootiocdibeantransactionorm.repository;

import org.example.springbootiocdibeantransactionorm.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Fetch Customer cùng với danh sách Order để tránh LazyInitializationException
    @Query("SELECT c FROM Customer c JOIN FETCH c.orders WHERE c.id = :id")
    Optional<Customer> findByIdWithOrders(@Param("id") Long id);
}
