package com.library;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/library")
@CrossOrigin
class LibraryController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    LibraryController(
            BookRepository bookRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository) {

        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // =========================
    // ISSUE BOOK
    // =========================

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    IssueResponse issueBook(@RequestBody IssueRequest request) {

        // Find user
        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        // Check user status
        if (user.getStatus() != User.Status.ACTIVE) {
            throw new IllegalArgumentException(
                    "User is inactive and cannot borrow books");
        }

        // Prevent duplicate active loan
        boolean alreadyIssued =
                transactionRepository.findByUserUserId(request.userId())
                        .stream()
                        .anyMatch(t ->
                                t.getBook().getBookId().equals(request.bookId())
                                        && t.getStatus() != Transaction.Status.RETURNED
                        );

        if (alreadyIssued) {
            throw new IllegalArgumentException(
                    "User already has this book issued");
        }

        // Find book
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Book not found"));

        // Check availability
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalArgumentException(
                    "Book is not available");
        }

        // Decrease available copies
        book.availableCopies--;

        // Create transaction
        Transaction transaction = new Transaction();

        transaction.book = book;
        transaction.user = user;
        transaction.issueDate = LocalDate.now();

        transaction.dueDate =
                LocalDate.now().plusDays(14);

        transaction.status =
                Transaction.Status.ISSUED;

        transaction.fineAmount =
                BigDecimal.ZERO;

        transactionRepository.save(transaction);

        return new IssueResponse(
                transaction.getTransactionId(),
                book.getTitle(),
                user.getFullName(),
                transaction.getIssueDate(),
                transaction.getDueDate(),
                transaction.getStatus()
        );
    }


    // =========================
    // RETURN BOOK
    // =========================

    @PostMapping("/return/{transactionId}")
    @Transactional
    ReturnResponse returnBook(
            @PathVariable Integer transactionId) {

        // Find transaction
        Transaction transaction =
                transactionRepository.findById(transactionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Transaction not found"));

        // Check already returned
        if (transaction.status ==
                Transaction.Status.RETURNED) {

            throw new IllegalArgumentException(
                    "Book has already been returned");
        }

        LocalDate returnDate = LocalDate.now();

        // Calculate overdue days
        long overdueDays = 0;

        if (returnDate.isAfter(transaction.dueDate)) {
            overdueDays = ChronoUnit.DAYS.between(
                    transaction.dueDate,
                    returnDate
            );
        }

        // Fine = ₹5 per overdue day
        BigDecimal fine =
                BigDecimal.valueOf(overdueDays * 5);

        // Update transaction
        transaction.returnDate = returnDate;
        transaction.fineAmount = fine;
        transaction.status =
                Transaction.Status.RETURNED;

        // Increase available copies
        Book book = transaction.book;
        book.availableCopies++;

        transactionRepository.save(transaction);

        return new ReturnResponse(
                transaction.getTransactionId(),
                book.getTitle(),
                transaction.getUser().getFullName(),
                transaction.getIssueDate(),
                transaction.getDueDate(),
                returnDate,
                fine,
                transaction.getStatus()
        );
    }
}


/*
 * Request used when issuing a book.
 *
 * Example:
 *
 * {
 *   "bookId": 4,
 *   "userId": 4
 * }
 */
record IssueRequest(
        Integer bookId,
        Integer userId
) {}


/*
 * Response returned after issuing a book.
 */
record IssueResponse(
        Integer transactionId,
        String bookTitle,
        String userName,
        LocalDate issueDate,
        LocalDate dueDate,
        Transaction.Status status
) {}


/*
 * Response returned after returning a book.
 */
record ReturnResponse(
        Integer transactionId,
        String bookTitle,
        String userName,
        LocalDate issueDate,
        LocalDate dueDate,
        LocalDate returnDate,
        BigDecimal fineAmount,
        Transaction.Status status
) {}