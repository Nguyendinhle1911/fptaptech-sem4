package org.example.springbootiocdibeantransactionorm.service;

import org.example.springbootiocdibeantransactionorm.entity.Customer;
import org.example.springbootiocdibeantransactionorm.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // ✅ Lấy tất cả khách hàng (Không fetch orders)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ✅ Lấy Customer mà không fetch orders (tránh query dư thừa nếu không cần)
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // ✅ Lấy Customer cùng với danh sách Order để tránh LazyInitializationException

    public Customer getCustomerWithOrders(Long id) {
        return customerRepository.findByIdWithOrders(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
