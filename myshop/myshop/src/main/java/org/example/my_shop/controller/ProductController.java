package org.example.my_shop.controller;

import org.example.my_shop.entity.Product;
import org.example.my_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    // Trang chính hiển thị danh sách sản phẩm với phân trang và tìm kiếm
    @GetMapping("/")
    public String viewHomePage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "sort", defaultValue = "id") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
            Model model) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sort);
        Page<Product> productPage;

        if (search != null && !search.isEmpty()) {
            productPage = productService.searchProducts(search, pageRequest);
        } else if (minPrice != null && maxPrice != null) {
            productPage = productService.findByPriceRange(minPrice, maxPrice, pageRequest);
        } else {
            productPage = productService.findAll(pageRequest);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);

        return "index";
    }

    // Hiển thị chi tiết sản phẩm
    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable("id") Long id, Model model) {
        Product product = productService.findById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "product-details";
        }
        return "redirect:/";
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
