package org.example.springbootiocdibeantransactionorm.repository;

import org.example.springbootiocdibeantransactionorm.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
