package org.example.springbootiocdibeantransactionorm.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootiocdibeantransactionorm.entity.Product;
import org.example.springbootiocdibeantransactionorm.service.CategoryService;
import org.example.springbootiocdibeantransactionorm.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor // Lombok giúp tự động tạo constructor với các biến final
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/list";
    }

    @GetMapping({"/add", "/edit/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        model.addAttribute("product", id == null ? new Product() : productService.findById(id)); // Nếu ID null thì tạo mới, ngược lại lấy từ DB
        model.addAttribute("categories", categoryService.findAll());
        return "product/form";
    }

    @PostMapping("/save")
    public String save(Product product, Long categoryId) {
        productService.save(product, categoryId);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
