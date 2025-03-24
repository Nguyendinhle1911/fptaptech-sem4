package org.example.springbootiocdibeantransactionorm.controller;

import lombok.AllArgsConstructor;
import org.example.springbootiocdibeantransactionorm.entity.Customer;
import org.example.springbootiocdibeantransactionorm.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", service.getAllCustomers());
        return "customers/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        return service.getCustomerById(id)
                .map(c -> { model.addAttribute("customer", c); return "customers/detail"; })
                .orElse("redirect:/customers?error=NotFound");
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/form";
    }

    @PostMapping
    public String save(@RequestParam String name, @RequestParam String email, @RequestParam String phoneNumber) {
        service.saveCustomer(new Customer(name, email, phoneNumber));
        return "redirect:/customers?success=Created";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        return view(id, model).replace("detail", "edit");
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestParam String name, @RequestParam String email, @RequestParam String phoneNumber) {
        return service.getCustomerById(id).map(c -> {
            c.setName(name);
            c.setEmail(email);
            c.setPhoneNumber(phoneNumber);
            service.saveCustomer(c);
            return "redirect:/customers?success=Updated";
        }).orElse("redirect:/customers?error=NotFound");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteCustomer(id);
        return "redirect:/customers?success=Deleted";
    }
}
