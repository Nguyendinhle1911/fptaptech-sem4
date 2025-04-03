package org.example.fetchtypeproblems.controller;

import org.example.fetchtypeproblems.model.Author;
import org.example.fetchtypeproblems.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // Display all authors
    @GetMapping
    public String getAllAuthors(Model model) {
        List<Author> authors = authorService.findAllAuthors();
        model.addAttribute("authors", authors);
        return "authors/list";
    }

    // Show form to create a new author
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("author", new Author());
        return "authors/form";
    }

    // Save a new author
    @PostMapping
    public String saveAuthor(@ModelAttribute("author") Author author) {
        authorService.createAuthor(author);
        return "redirect:/authors";
    }

    // Show form to edit an author
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Author author = authorService.findAuthorById(id).orElseThrow(() -> new RuntimeException("Author not found"));
        model.addAttribute("author", author);
        return "authors/form";
    }

    // Update an existing author
    @PostMapping("/{id}")
    public String updateAuthor(@PathVariable Long id, @ModelAttribute("author") Author author) {
        authorService.updateAuthor(id, author);
        return "redirect:/authors";
    }

    // Delete an author
    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }
}
