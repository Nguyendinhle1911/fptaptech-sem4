package org.example.fetchtypeproblems.repository;

import org.example.fetchtypeproblems.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a JOIN a.books b WHERE b.id = :bookId")
    List<Author> findAuthorsByBookId(@Param("bookId") Long bookId);

    @Query("SELECT a FROM Author a WHERE lower(a.name) LIKE lower(concat('%', :name, '%'))")
    List<Author> findByNameContainingIgnoreCase(@Param("name") String name);
}
