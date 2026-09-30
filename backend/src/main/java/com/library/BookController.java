package com.library;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Entity
@Table(name = "books")
class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    Integer bookId;

    @NotBlank
    String title;

    @NotBlank
    @Column(unique = true)
    String isbn;

    @Column(name = "publication_year", columnDefinition = "YEAR")
    java.time.Year publicationYear;

    @Column(name = "total_copies")
    int totalCopies;

    @Column(name = "available_copies")
    int availableCopies;

    @Column(name = "shelf_location")
    String shelfLocation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    Author author;

    public Integer getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public java.time.Year getPublicationYear() {
        return publicationYear;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public Category getCategory() {
        return category;
    }

    public Author getAuthor() {
        return author;
    }
}

@Entity
@Table(name = "categories")
class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    Integer categoryId;

    @Column(name = "category_name")
    String categoryName;

    String description;

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}

@Entity
@Table(name = "authors")
class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    Integer authorId;

    @Column(name = "author_name")
    String authorName;

    String country;

    public Integer getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }
}

@RestController
@RequestMapping("/api/books")
@CrossOrigin
class BookController {

    private final BookRepository repo;

    BookController(BookRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    List<Book> all(
            @RequestParam(required = false) String q) {

        return q == null
                ? repo.findAll()
                : repo.findByTitleContainingIgnoreCase(q);
    }

    @GetMapping("/{id}")
    Book one(@PathVariable Integer id) {

        return repo.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Book not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Book create(@Valid @RequestBody Book book) {

        return repo.save(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Integer id) {

        repo.deleteById(id);
    }
}

@RestController
class HealthController {

    @GetMapping("/api/health")
    Map<String, Object> health() {

        return Map.of(
                "status", "UP",
                "service", "Library Management API",
                "time", Instant.now().toString()
        );
    }
}