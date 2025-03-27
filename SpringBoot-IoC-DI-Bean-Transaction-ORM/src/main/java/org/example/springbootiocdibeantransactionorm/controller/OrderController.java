package org.example.springbootiocdibeantransactionorm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

    // ✅ Hiển thị danh sách đơn hàng
    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders/list";
    }

    // ✅ Xem chi tiết đơn hàng cùng Customer & OrderItems
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            Order order = orderService.getOrderWithDetails(id);
            model.addAttribute("order", order);
            return "orders/detail";
        } catch (RuntimeException e) {
            log.error("Order not found: {}", id, e);
            return "redirect:/orders?error=NotFound";
        }
    }

    // ✅ Form tạo mới đơn hàng
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        return "orders/form";
    }

    // ✅ Lưu đơn hàng mới
    @PostMapping
    public String save(@RequestParam Long customerId, @RequestParam List<Long> productIds, @RequestParam List<Integer> quantities) {
        try {
            var customer = customerService.getCustomerById(customerId);
            var order = new Order(customer, LocalDateTime.now());

            boolean hasValidProduct = false;
            for (int i = 0; i < productIds.size(); i++) {
                var product = productService.getProductById(productIds.get(i));
                if (quantities.get(i) > 0) {
                    order.getOrderItems().add(new OrderItem(order, product, quantities.get(i), product.getPrice()));
                    hasValidProduct = true;
                }
            }

            if (!hasValidProduct) {
                log.warn("No valid product selected for order, customer ID: {}", customerId);
                return "redirect:/orders/new?error=NoProductSelected";
            }

            return saveAndRedirect(order);
        } catch (RuntimeException e) {
            log.error("Error creating order for customer ID: {}", customerId, e);
            return "redirect:/orders/new?error=CustomerOrProductNotFound";
        }
    }

    private String saveAndRedirect(Order order) {
        orderService.saveOrder(order);
        log.info("Order created successfully: {}", order.getId());
        return "redirect:/orders?success=Created";
    }

    // ✅ Xóa đơn hàng
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            log.info("Order deleted: {}", id);
            return "redirect:/orders?success=Deleted";
        } catch (RuntimeException e) {
            log.error("Error deleting order: {}", id, e);
            return "redirect:/orders?error=DeleteFailed";
        }
    }
}
