package org.example.springbootiocdibeantransactionorm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootiocdibeantransactionorm.entity.Customer;
import org.example.springbootiocdibeantransactionorm.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
@AllArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerService service;

    // ✅ Danh sách khách hàng (Không fetch orders)
    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", service.getAllCustomers());
        return "customers/list";
    }

    // ✅ Xem chi tiết khách hàng cùng với danh sách đơn hàng
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            Customer customer = service.getCustomerWithOrders(id);
            model.addAttribute("customer", customer);
            return "customers/detail";
        } catch (RuntimeException e) {
            log.error("Customer not found: {}", id, e);
            return "redirect:/customers?error=NotFound";
        }
    }

    // ✅ Form tạo mới khách hàng
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/form";
    }

    // ✅ Lưu khách hàng mới và chuyển hướng đến trang sửa ngay lập tức
    @PostMapping
    public String save(@RequestParam String name, @RequestParam String email, @RequestParam String phoneNumber) {
        try {
            Customer customer = service.saveCustomer(new Customer(name, email, phoneNumber));
            log.info("Customer created successfully: {}", customer.getId());
            return "redirect:/customers/edit/" + customer.getId();
        } catch (Exception e) {
            log.error("Error creating customer", e);
            return "redirect:/customers?error=CreateFailed";
        }
    }

    // ✅ Chỉnh sửa khách hàng
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        try {
            Customer customer = service.getCustomerById(id);
            model.addAttribute("customer", customer);
            return "customers/edit";
        } catch (RuntimeException e) {
            log.error("Customer not found: {}", id, e);
            return "redirect:/customers?error=NotFound";
        }
    }

    // ✅ Cập nhật thông tin khách hàng
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestParam String name,
                         @RequestParam String email, @RequestParam String phoneNumber) {
        try {
            Customer customer = service.getCustomerById(id);
            customer.setName(name);
            customer.setEmail(email);
            customer.setPhoneNumber(phoneNumber);
            service.saveCustomer(customer);
            log.info("Customer updated: {}", id);
            return "redirect:/customers?success=Updated";
        } catch (RuntimeException e) {
            log.error("Error updating customer: {}", id, e);
            return "redirect:/customers?error=UpdateFailed";
        }
    }

    // ✅ Xóa khách hàng
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            service.deleteCustomer(id);
            log.info("Customer deleted: {}", id);
            return "redirect:/customers?success=Deleted";
        } catch (RuntimeException e) {
            log.error("Error deleting customer: {}", id, e);
            return "redirect:/customers?error=DeleteFailed";
        }
    }
}
