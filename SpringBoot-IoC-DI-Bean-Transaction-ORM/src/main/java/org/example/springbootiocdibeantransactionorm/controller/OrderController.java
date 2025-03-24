package org.example.springbootiocdibeantransactionorm.controller;

import lombok.AllArgsConstructor;
import org.example.springbootiocdibeantransactionorm.entity.Order;
import org.example.springbootiocdibeantransactionorm.entity.OrderItem;
import org.example.springbootiocdibeantransactionorm.service.OrderService;
import org.example.springbootiocdibeantransactionorm.service.CustomerService;
import org.example.springbootiocdibeantransactionorm.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        return orderService.getOrderById(id)
                .map(o -> { model.addAttribute("order", o); return "orders/detail"; })
                .orElse("redirect:/orders?error=NotFound");
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        return "orders/form";
    }

    @PostMapping
    public String save(@RequestParam Long customerId, @RequestParam List<Long> productIds, @RequestParam List<Integer> quantities) {
        var customer = customerService.getCustomerById(customerId).orElseThrow();
        var order = new Order(customer, LocalDateTime.now());

        boolean hasValidProduct = false;
        for (int i = 0; i < productIds.size(); i++) {
            var product = productService.getProductById(productIds.get(i)).orElseThrow();
            if (quantities.get(i) > 0) {
                order.getOrderItems().add(new OrderItem(order, product, quantities.get(i), product.getPrice()));
                hasValidProduct = true;
            }
        }

        return hasValidProduct ? saveAndRedirect(order) : "redirect:/orders/new?error=NoProductSelected";
    }

    private String saveAndRedirect(Order order) {
        orderService.saveOrder(order);
        return "redirect:/orders?success=Created";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders?success=Deleted";
    }
}
