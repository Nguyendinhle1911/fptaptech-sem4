package org.example.my_shop.controller;

import org.example.my_shop.entity.Product;
import org.example.my_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    // Trang chính hiển thị danh sách sản phẩm
    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("products", productService.findAll());
        return "index"; // Tên template hiển thị danh sách sản phẩm
    }

    // Hiển thị form thêm sản phẩm
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "create"; // Tên template hiển thị form thêm sản phẩm
    }

    // Xử lý thêm sản phẩm
    @PostMapping("/create")
    public String createProduct(@ModelAttribute("product") Product product) {
        productService.save(product);
        return "redirect:/"; // Quay lại trang chính sau khi thêm
    }

    // Hiển thị form cập nhật sản phẩm
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "update"; // Tên template hiển thị form cập nhật
        }
        return "redirect:/"; // Nếu không tìm thấy sản phẩm, quay lại trang chính
    }

    // Xử lý cập nhật sản phẩm
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") Product product) {
        product.setId(id);
        productService.save(product);
        return "redirect:/"; // Quay lại trang chính sau khi cập nhật
    }

    // Xử lý xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return "redirect:/"; // Quay lại trang chính sau khi xóa
    }
}
