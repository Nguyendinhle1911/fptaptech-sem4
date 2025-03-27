package org.example.springbootiocdibeantransactionorm.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootiocdibeantransactionorm.entity.Product;
import org.example.springbootiocdibeantransactionorm.service.ProductService;
import org.example.springbootiocdibeantransactionorm.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
@AllArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    // ✅ Hiển thị danh sách sản phẩm
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products/list";
    }

    // ✅ Xem chi tiết sản phẩm cùng Category
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        try {
            Product product = productService.getProductWithCategory(id);
            model.addAttribute("product", product);
            return "products/detail";
        } catch (RuntimeException e) {
            log.error("Product not found: {}", id, e);
            return "redirect:/products?error=NotFound";
        }
    }

    // ✅ Form tạo mới sản phẩm
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/form";
    }

    // ✅ Lưu sản phẩm mới
    @PostMapping
    public String save(@ModelAttribute Product product) {
        try {
            productService.saveProduct(product);
            log.info("Product created successfully: {}", product.getId());
            return "redirect:/products?success=Created";
        } catch (Exception e) {
            log.error("Error creating product", e);
            return "redirect:/products?error=CreateFailed";
        }
    }

    // ✅ Chỉnh sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        try {
            Product product = productService.getProductWithCategory(id);
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "products/edit";
        } catch (RuntimeException e) {
            log.error("Product not found: {}", id, e);
            return "redirect:/products?error=NotFound";
        }
    }

    // ✅ Cập nhật sản phẩm
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestParam String name, @RequestParam double price,
                         @RequestParam String description, @RequestParam Long categoryId) {
        try {
            Product product = productService.getProductById(id);
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setCategory(categoryService.getCategoryById(categoryId));
            productService.saveProduct(product);
            log.info("Product updated: {}", id);
            return "redirect:/products?success=Updated";
        } catch (RuntimeException e) {
            log.error("Error updating product: {}", id, e);
            return "redirect:/products?error=UpdateFailed";
        }
    }

    // ✅ Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            log.info("Product deleted: {}", id);
            return "redirect:/products?success=Deleted";
        } catch (RuntimeException e) {
            log.error("Error deleting product: {}", id, e);
            return "redirect:/products?error=DeleteFailed";
        }
    }
}
