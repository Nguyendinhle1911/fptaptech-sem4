package org.example.springbootiocdibeantransactionorm.controller;

import org.example.springbootiocdibeantransactionorm.entity.Order;
import org.example.springbootiocdibeantransactionorm.entity.OrderItem;
import org.example.springbootiocdibeantransactionorm.entity.Product;
import org.example.springbootiocdibeantransactionorm.service.OrderService;
import org.example.springbootiocdibeantransactionorm.service.CustomerService;
import org.example.springbootiocdibeantransactionorm.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderController(OrderService orderService, CustomerService customerService, ProductService productService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.productService = productService;
    }

    // Danh sách đơn hàng
    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    // Xem chi tiết đơn hàng
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.getOrderById(id);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            return "orders/detail";
        } else {
            return "redirect:/orders?error=OrderNotFound";
        }
    }

    // Hiển thị form tạo đơn hàng
    @GetMapping("/new")
    public String showOrderForm(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("products", productService.getAllProducts());
        return "orders/form";
    }

    // Lưu đơn hàng mới
    @PostMapping
    public String saveOrder(
            @RequestParam Long customerId,
            @RequestParam List<Long> productIds,
            @RequestParam List<Integer> quantities) {

        var customer = customerService.getCustomerById(customerId).orElseThrow();
        var order = new Order(customer, LocalDateTime.now());

        boolean hasValidProduct = false;

        for (int i = 0; i < productIds.size(); i++) {
            var product = productService.getProductById(productIds.get(i)).orElseThrow();
            var quantity = quantities.get(i);

            if (quantity > 0) {
                order.getOrderItems().add(new OrderItem(order, product, quantity, product.getPrice()));
                hasValidProduct = true;
            }
        }

        if (!hasValidProduct) {
            return "redirect:/orders/new?error=NoProductSelected";
        }

        orderService.saveOrder(order);
        return "redirect:/orders?success=OrderCreated";
    }

    // Xóa đơn hàng
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders?success=OrderDeleted";
    }
}
