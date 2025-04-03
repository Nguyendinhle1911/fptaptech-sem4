package org.example.fetchtypeproblems.repository;

import org.example.fetchtypeproblems.model.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = "Book.authors", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
    Optional<Book> findBookWithAuthors(@Param("bookId") Long bookId);

    @EntityGraph(value = "Book.authors", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT b FROM Book b")
    List<Book> findAllBooksWithAuthors();

    @Query("SELECT b FROM Book b WHERE lower(b.title) LIKE lower(concat('%', :title, '%'))")
    List<Book> findByTitleContainingIgnoreCase(@Param("title") String title);
}
