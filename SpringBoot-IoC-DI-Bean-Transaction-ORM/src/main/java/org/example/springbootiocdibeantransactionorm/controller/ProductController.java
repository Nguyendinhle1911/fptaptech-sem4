package org.example.springbootiocdibeantransactionorm.controller;

import lombok.AllArgsConstructor;
import org.example.springbootiocdibeantransactionorm.entity.Product;
import org.example.springbootiocdibeantransactionorm.service.ProductService;
import org.example.springbootiocdibeantransactionorm.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        return productService.getProductById(id)
                .map(p -> { model.addAttribute("product", p); return "products/detail"; })
                .orElse("redirect:/products?error=NotFound");
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/form";
    }

    @PostMapping
    public String save(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products?success=Created";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        return productService.getProductById(id).map(p -> {
            model.addAttribute("product", p);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "products/edit";
        }).orElse("redirect:/products?error=NotFound");
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestParam String name, @RequestParam double price,
                         @RequestParam String description, @RequestParam Long categoryId) {
        return productService.getProductById(id).map(p -> {
            p.setName(name);
            p.setPrice(price);
            p.setDescription(description);
            p.setCategory(categoryService.getCategoryById(categoryId).orElseThrow());
            productService.saveProduct(p);
            return "redirect:/products?success=Updated";
        }).orElse("redirect:/products?error=NotFound");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products?success=Deleted";
    }
}
