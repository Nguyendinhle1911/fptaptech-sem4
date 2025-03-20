package org.example.springbootiocdibeantransactionorm.controller;

import org.example.springbootiocdibeantransactionorm.entity.Product;
import org.example.springbootiocdibeantransactionorm.service.ProductService;
import org.example.springbootiocdibeantransactionorm.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // Hiển thị danh sách sản phẩm
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/list";
    }

    // Xem chi tiết sản phẩm
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "products/detail";
        } else {
            return "redirect:/products?error=ProductNotFound";
        }
    }

    // Hiển thị form thêm sản phẩm mới
    @GetMapping("/new")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/form";
    }

    // Lưu sản phẩm mới
    @PostMapping
    public String saveProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products?success=ProductCreated";
    }

    // Hiển thị form chỉnh sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "products/edit";
        } else {
            return "redirect:/products?error=ProductNotFound";
        }
    }

    // Cập nhật sản phẩm
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String name,
                                @RequestParam double price,
                                @RequestParam String description,
                                @RequestParam Long categoryId) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setCategory(categoryService.getCategoryById(categoryId).orElseThrow());
            productService.saveProduct(product);
            return "redirect:/products?success=ProductUpdated";
        } else {
            return "redirect:/products?error=ProductNotFound";
        }
    }

    // Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products?success=ProductDeleted";
    }
}
