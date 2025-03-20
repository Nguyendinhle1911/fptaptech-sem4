package org.example.springbootiocdibeantransactionorm.controller;

import org.example.springbootiocdibeantransactionorm.entity.Customer;
import org.example.springbootiocdibeantransactionorm.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.getAllCustomers();
        model.addAttribute("customers", customers);
        return "customers/list";
    }

    @GetMapping("/{id}")
    public String viewCustomer(@PathVariable Long id, Model model) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            model.addAttribute("customer", customer.get());
            return "customers/detail";
        } else {
            return "redirect:/customers?error=CustomerNotFound";
        }
    }

    @GetMapping("/new")
    public String showCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/form";
    }

    @PostMapping
    public String saveCustomer(@RequestParam String name, @RequestParam String email, @RequestParam String phoneNumber) {
        Customer customer = new Customer(name, email, phoneNumber);
        customerService.saveCustomer(customer);
        return "redirect:/customers?success=CustomerCreated";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            model.addAttribute("customer", customer.get());
            return "customers/edit";
        } else {
            return "redirect:/customers?error=CustomerNotFound";
        }
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id,
                                 @RequestParam String name,
                                 @RequestParam String email,
                                 @RequestParam String phoneNumber) {
        Optional<Customer> customerOpt = customerService.getCustomerById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setName(name);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            customerService.saveCustomer(customer);
            return "redirect:/customers?success=CustomerUpdated";
        } else {
            return "redirect:/customers?error=CustomerNotFound";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers?success=CustomerDeleted";
    }
}
