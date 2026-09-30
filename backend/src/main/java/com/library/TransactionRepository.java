package com.library;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByStatus(Transaction.Status status);

    List<Transaction> findByUserUserId(Integer userId);

    List<Transaction> findByBookBookId(Integer bookId);
}