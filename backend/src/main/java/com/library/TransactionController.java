package com.library;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "transactions")
class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    Integer transactionId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "issue_date")
    LocalDate issueDate;

    @Column(name = "due_date")
    LocalDate dueDate;

    @Column(name = "return_date")
    LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    Status status;

    @Column(name = "fine_amount")
    BigDecimal fineAmount;

    public Integer getTransactionId() {
        return transactionId;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public Status getStatus() {
        return status;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    enum Status {
        ISSUED,
        RETURNED,
        OVERDUE
    }
}

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin
class TransactionController {

    private final TransactionRepository repo;

    TransactionController(TransactionRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    List<Transaction> all(
            @RequestParam(required = false)
            Transaction.Status status) {

        return status == null
                ? repo.findAll()
                : repo.findByStatus(status);
    }

    @GetMapping("/{id}")
    Transaction one(@PathVariable Integer id) {

        return repo.findById(id)
                .orElseThrow(() ->
                        new java.util.NoSuchElementException(
                                "Transaction not found"));
    }

    @GetMapping("/user/{userId}")
    List<Transaction> byUser(
            @PathVariable Integer userId) {

        return repo.findByUserUserId(userId);
    }

    @GetMapping("/book/{bookId}")
    List<Transaction> byBook(
            @PathVariable Integer bookId) {

        return repo.findByBookBookId(bookId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Transaction create(
            @Valid @RequestBody Transaction transaction) {

        return repo.save(transaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Integer id) {

        repo.deleteById(id);
    }
}