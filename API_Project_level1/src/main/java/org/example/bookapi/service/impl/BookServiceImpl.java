package org.example.bookapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.bookapi.config.RabbitMQConfig;
import org.example.bookapi.dto.BookRequest;
import org.example.bookapi.dto.BookResponse;
import org.example.bookapi.entity.Book;
import org.example.bookapi.exception.BookNotFoundException;
import org.example.bookapi.repository.BookRepository;
import org.example.bookapi.service.BookService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @CacheEvict(value = {"book", "books"}, allEntries = true)
    public BookResponse createBook(BookRequest bookRequest) {
        Book book = Book.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .build();

        Book savedBook = bookRepository.save(book);
        sendBookNotificationAsync(savedBook, "created"); // Gửi thông báo bất đồng bộ
        return mapToResponse(savedBook);
    }

    @Override
    @CacheEvict(value = {"book", "books"}, key = "#id")
    public BookResponse updateBook(Long id, BookRequest bookRequest) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        existingBook.setTitle(bookRequest.getTitle());
        existingBook.setAuthor(bookRequest.getAuthor());

        Book updatedBook = bookRepository.save(existingBook);
        sendBookNotificationAsync(updatedBook, "updated"); // Gửi thông báo bất đồng bộ
        return mapToResponse(updatedBook);
    }

    @Override
    @Cacheable(value = "book", key = "#id")
    public BookResponse getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return mapToResponse(book);
    }

    @Override
    @Cacheable(value = "books", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @CacheEvict(value = {"book", "books"}, key = "#id")
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    @Async
    public void sendBookNotificationAsync(Book book, String action) {
        String message = String.format("Book %s (ID: %d) has been %s", book.getTitle(), book.getId(), action);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, message);
    }

    private BookResponse mapToResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .build();
    }
}