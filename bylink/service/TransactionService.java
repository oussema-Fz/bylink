package com.bylink.service;

import com.bylink.entity.*;
import com.bylink.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final ProductService productService;

    @Transactional
    public Transaction createPayment(User client, String productLink, boolean fullPayment) {
        Product product = productService.getProductByLink(productLink);
        BigDecimal amount = fullPayment ? 
            product.getPrice().add(product.getDeliveryFee()) : 
            product.getDeliveryFee();

        Wallet clientWallet = walletService.getWalletByUser(client);
        if (clientWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        Transaction transaction = new Transaction();
        transaction.setSender(client);
        transaction.setReceiver(product.getMerchant());
        transaction.setProduct(product);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.PAYMENT);
        transaction.setStatus(fullPayment ? TransactionStatus.IN_ESCROW : TransactionStatus.COMPLETED);

        walletService.deposit(client, amount.negate());
        if (!fullPayment) {
            walletService.deposit(product.getMerchant(), amount);
        }

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction setTrackingId(Long transactionId, String trackingId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setTrackingId(trackingId);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction completeEscrow(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.IN_ESCROW) {
            throw new RuntimeException("Transaction is not in escrow");
        }

        walletService.deposit(transaction.getReceiver(), transaction.getAmount());
        transaction.setStatus(TransactionStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction refundEscrow(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.IN_ESCROW) {
            throw new RuntimeException("Transaction is not in escrow");
        }

        walletService.deposit(transaction.getSender(), transaction.getAmount());
        transaction.setStatus(TransactionStatus.REFUNDED);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(User user) {
        return transactionRepository.findBySender(user);
    }

    public List<Transaction> getMerchantTransactions(User merchant) {
        return transactionRepository.findByReceiver(merchant);
    }
}
