package com.bylink.repository;

import com.bylink.entity.Transaction;
import com.bylink.entity.User;
import com.bylink.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySender(User sender);
    List<Transaction> findByReceiver(User receiver);
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findByTrackingId(String trackingId);
}
