package org.example.fetchtypeproblems.controller;

import org.example.fetchtypeproblems.model.Author;
import org.example.fetchtypeproblems.model.Book;
import org.example.fetchtypeproblems.service.AuthorService;
import org.example.fetchtypeproblems.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    // Hiển thị danh sách sách
    @GetMapping
    public String getAllBooks(Model model) {
        List<Book> books = bookService.findAllBooks(); // Lấy danh sách tất cả sách
        model.addAttribute("books", books);
        return "books/list"; // Trả về template danh sách sách
    }

    // Hiển thị form tạo sách mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book()); // Tạo một sách mới
        loadAuthorsIntoModel(model); // Thêm danh sách tác giả vào Model
        return "books/form"; // Trả về template form
    }

    // Lưu sách mới
    @PostMapping
    public String saveBook(@ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (isInvalidBook(book, result, model)) {
            return "books/form"; // Trả lại form nếu có lỗi
        }
        bookService.createBook(book); // Lưu sách
        return "redirect:/books"; // Chuyển hướng về danh sách sách
    }

    // Hiển thị form chỉnh sửa sách
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.findBookById(id)
                .orElseThrow(() -> new RuntimeException("Book not found")); // Ném lỗi nếu không tìm thấy sách
        model.addAttribute("book", book);
        loadAuthorsIntoModel(model); // Thêm danh sách tác giả vào Model
        return "books/form"; // Trả về template form chỉnh sửa
    }

    // Cập nhật sách
    @PostMapping("/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute("book") Book book, BindingResult result, Model model) {
        if (isInvalidBook(book, result, model)) {
            return "books/form"; // Trả lại form nếu có lỗi
        }
        bookService.updateBook(id, book); // Cập nhật sách
        return "redirect:/books"; // Chuyển hướng về danh sách sách
    }

    // Xóa sách
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id); // Xóa sách
        return "redirect:/books"; // Chuyển hướng về danh sách sách
    }

    // Helper: Tải danh sách tác giả vào Model
    private void loadAuthorsIntoModel(Model model) {
        List<Author> authors = authorService.findAllAuthors();
        if (authors == null || authors.isEmpty()) {
            throw new RuntimeException("No authors available. Please add authors first.");
        }
        model.addAttribute("authors", authors);
    }

    // Helper: Kiểm tra tính hợp lệ của sách
    private boolean isInvalidBook(Book book, BindingResult result, Model model) {
        if (book.getAuthors() == null || book.getAuthors().isEmpty()) {
            result.rejectValue("authors", "error.authors", "A book must have at least one author.");
            loadAuthorsIntoModel(model);
            return true;
        }
        return false;
    }
}
